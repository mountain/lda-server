package lancelot;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.mallet.types.Instance;
import cc.mallet.pipe.Pipe;

import transliterial.Junidecode;

public class FilterPipe extends Pipe {

    public static String filter(String content) {
        //logger.info("filter css start...");
        //Matcher matcher = chkpat.matcher(content);
        //int start = -1, end = -1;
        //while (matcher.find()) {
        //    if (start == -1) {
        //        start = matcher.start();
        //        start = Math.max(start - 50, 0);
        //    }
        //    end = matcher.end();
        //}
        //if (start != -1 && end != -1) {
        //    StringBuilder head = new StringBuilder(content.substring(0, start));
        //    StringBuilder test = new StringBuilder(content.substring(start, end));
        //    StringBuilder tail = new StringBuilder(content.substring(end));
        //
        //    matcher = csspat.matcher(test);
        //    StringBuilder builder = head;
        //    if (matcher.find()) {
        //        start = matcher.start();
        //        for (int i = 0; i < start; i++) {
        //            builder.append(test.charAt(i));
        //        }
        //    }
        //
        //    content = builder.append(tail).toString();
        //}
        //logger.info("filter css end...");

        Matcher matcher = wwwpat.matcher(content);
        if (matcher.find()) {
            content = matcher.replaceAll("");
        }

        content = content.replace(',', ' ');
        content = content.replace('.', ' ');
        content = content.replace('(', ' ');
        content = content.replace(')', ' ');
        content = content.replace('[', ' ');
        content = content.replace(']', ' ');
        content = content.replace('{', ' ');
        content = content.replace('}', ' ');
        content = content.replace(';', ' ');
        content = content.replace('?', ' ');
        content = content.replace('/', ' ');
        content = content.replace('\\', ' ');
        content = content.replace('"', ' ');
        content = content.replace('<', ' ');
        content = content.replace('>', ' ');
        content = content.replace('!', ' ');
        content = content.replace(':', ' ');
        content = content.replace('\'', ' ');
        content = content.replace('@', ' ');
        content = content.replace('=', ' ');
        content = content.replace('#', ' ');
        content = content.replace('^', ' ');
        content = content.replace('-', ' ');
        content = content.replace('_', ' ');
        content = content.replace('|', ' ');
        content = content.replace('~', ' ');
        content = content.replace('`', ' ');
        content = content.replace('*', ' ');
        content = content.replace('&', ' ');
        content = content.replace('$', ' ');
        content = content.replace('–', ' ');
        content = content.replaceAll("[0-9]{5,20}", " ");
        content = content.replaceAll("(?<=[0-9])(?=[a-zA-Z]{2,20})", " ");
        content = content.replaceAll("(?<=[a-zA-Z]{2,20})(?=[0-9])", " ");
        return content.toLowerCase();
    }

    static final long serialVersionUID = -4406607419111419705L;
    static final Logger logger = LoggerFactory.getLogger(java.lang.Class.class);

    public static transient final Pattern spaces = Pattern.compile("\\s+");
    public static transient final Pattern chkpat = Pattern.compile("\\{[^{]+:[^}]+}(._)*");
    public static transient final Pattern csspat = Pattern.compile("((\\.|\\w|\\d|-|_)+(\\s|,))*(!DOCTYPE|html|head|title|body|h1|p|br|hr|acronym|abbr|address|b|bdi|bdo|big|blockquote|center|cite|code|del|dfn|em|font|i|ins|kbd|mark|meter|pre|progress|q|rp|rt|ruby|s|samp|small|strike|strong|sub|sup|template|time|tt|u|var|wbr|form|input|textarea|button|select|optgroup|option|label|fieldset|legend|datalist|output|frame|frameset|noframes|iframe|img|map|area|canvas|figcaption|figure|picture|svg|audio|source|track|video|a|link|nav|ul|ol|li|dir|dl|dt|dd|table|caption|th|tr|td|thead|tbody|tfoot|col|colgroup|style|div|span|header|footer|main|section|article|aside|details|dialog|summary|data|meta|base|basefont|script|noscript|applet|embed|object|param)*(|-|_|\\.|,|>|\\w)+(:\\w+)*\\{[^{]+:[^}]+}(._)*");
    public static transient final Pattern wwwpat = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private static transient int counter = 0;

    private Set stopwords;

    public FilterPipe (Set stopwords) {
        this.stopwords = stopwords;
    }

    public Instance pipe(Instance inst) {
        counter += 1;
        logger.info(counter + ": " + inst.getName());
        if (inst.getName().toString().endsWith(".txt")) {
            StringBuilder builder = new StringBuilder();
            String content = inst.getData().toString();
            content = filter(content);
            String[] words = spaces.split(content, 0);
            for (int i = 0; i < words.length; i++) {
                //logger.info(counter + ": processing" + inst.getName() + String.format(" %06d", i));
                if (!stopwords.contains(words[i])) {
                    //logger.info(counter + ": processing_1" + inst.getName() + String.format(" %06d", i));
                    String liter = Junidecode.unidecode(words[i]);
                    int len = liter.length();
                    if (1 < len && len < 20) {
                        builder.append(liter);
                        builder.append(" ");
                    }
                    //logger.info(counter + ": processing_2" + inst.getName() + String.format(" %06d", i));
                }
            }
            content = builder.toString();
            inst.setData(content);
            logger.info(inst.getData().toString());
        } else {
            inst.setData("");
        }
        logger.info(counter + ": finished" + inst.getName());
        return inst;
    }

    public static void main(String[] args) {
        String text = "//pages.lazada.com.my/wow/i/my/LandingPage/flashsale?wh_weex=true&wx_navbar_transparent=true&scm=1003.4.icms-zebra-5000097-2585837.OTHER_5717379203_3345780\"\n" +
                "{\"data\":[{\"data_default\":{\"length\":200},\"data_param\":{\"appId\":\"icms-zebra-5000097-2585701\",\"terminalType\":\"1\"},\"data_source\":\"ald\",\"data_type\":\"jsonp\",\"data_url\":\"mtop.lazada.pegasus.service.AldRecommendService.recommend2\",\"dynamic_param\":{\"appId\":\"icms-zebra-5000097-2585701\",\"isCampaignPage\":false,\"resourceCode\":\"icms-zebra-5000097-2585701\",\"terminalType\":\"1\"}}],\"label\":{\"id\":\"5195935256\",\"title\":\"Just For You\"}}";
        filter(text);
    }
}
