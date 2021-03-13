package vip.xianyu.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:asus
 * @Date: 2021/3/10 21:20
 * @Description:敏感词过滤器
 */
@Component
public class SensitiveFilter {
    private static final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT="***";
    //根节点
    private TrieNode rootNode=new TrieNode();

    @PostConstruct
    public void init(){
        try(
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader=new BufferedReader(new InputStreamReader(resourceAsStream));
        ) {
            String keyword;
            while((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
           logger.error("加载敏感词文件失败:"+e.getMessage());
        }

    }
    //将敏感词添加到前缀树
    private void addKeyword(String keyword){
        //申明一个临时节点，用于标记当前节点
        TrieNode tmpNode=rootNode;
        for(int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode subNode = tmpNode.getSubNode(c);
            if(null==subNode){
                //初始化子节点
                subNode=new TrieNode();
                tmpNode.addSubNode(c,subNode);
            }
            tmpNode=subNode;
            if(i==keyword.length()-1){
                tmpNode.setKeywordEnd(true);
            }
        }
    }
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针一
        TrieNode tmpNode=rootNode;
        //指针二
        int begin =0;
        //指针三
        int position=0;
        StringBuilder sb=new StringBuilder();
        while(position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //指针一处于根节点,将此符号计入结果,让指针二向下走一步
                if(tmpNode==rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头或中间，指针三都向下走一步
                position++;
                continue;
            }
            //检查下级节点
            tmpNode=tmpNode.getSubNode(c);
            if(tmpNode==null){
                //以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                ++begin;
                ++position;
                //重新指向根节点
                tmpNode=rootNode;
            }else if(tmpNode.isKeywordEnd()){
                //发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                ++position;
                begin=position;
                //重新指向根节点
                tmpNode=rootNode;
            }else{
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为字符
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) &&(c<0x2E80 || c>0x9FFF);
    }

    private class TrieNode{
        //关键词结束标识
        private boolean isKeywordEnd=false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes=new HashMap<>();
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
