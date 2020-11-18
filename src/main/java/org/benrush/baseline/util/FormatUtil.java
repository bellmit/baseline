package org.benrush.baseline.util;

import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.BaseLineException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FormatUtil {
    private FormatUtil(){

    }
    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line) {
        if (line == null) {
            return null;
        }
        // 分成数组
        char[] charArray = line.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */

    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    /**
     * @Description: 迭代访问字符串进行替换的对象
     * @author: 刘希晨
     * @date:  2020/11/13 15:12
     */
    static class Iter {
        private String line;
        private Integer pos;
        Iter(String line,Integer pos){
            this.line = line;
            this.pos = pos;
        }

        public String getLine() {
            return line;
        }

        public Integer getPos() {
            return pos;
        }
    }

    /**
     * @Description: 替换掉${}占位符中的内容
     * @author: 刘希晨
     * @date:  2020/11/13 10:52
     */
    public static String process(String line){
        Iter iter = new Iter(line,0);
        while (iter.getPos() != -1){
            iter = process(iter);
        }
        return iter.getLine();
    }

    public static Iter process(Iter iter){
        Integer position = iter.getPos();
        String line = iter.getLine();
        for(int i = position;i < line.length();i++){
            char c = line.charAt(i);
            if(c == '$'){
                //扫描到$符号，进入替换模式
                if(line.charAt(i+1) == '{'){
                    //扫描到{符号
                    int pos = i+2;
                    while(line.charAt(pos) != '}'){
                        pos++;
                    }
                    String key = line.substring(i+2,pos);
                    String value = ConfigUtil.getConfig(key);
                    StringBuilder builder = new StringBuilder();
                    builder.append(line.substring(0,i));
                    builder.append(value);
                    builder.append(line.substring(pos+1));
                    //新的位置已经变化了，因为字符串的长度发生了变化
                    return new Iter(builder.toString(), i + value.length());
                }else{
                    log.info(line);
                    throw new BaseLineException("格式解析错误");
                }
            }
        }
        return new Iter(line,-1);
    }
}
