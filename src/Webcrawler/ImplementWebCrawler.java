package Webcrawler;


import java.util.List;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 *     public List<String> getUrls(String url) {}
 * }
 */
//class Solution {
//
//    public String getHostName(String url){
//        return url.substring(7).split("/")[0];
//    }
//
//    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
//        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
//        Set<String> visited = new HashSet<>();
//        Queue<Future> taskQueue = new LinkedList<>();
//
//        String startHost = getHostName(startUrl);
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        queue.offer(startUrl);
//
//        while(true){
//            String curr = queue.poll();
//            if(curr!=null){
//                if(!getHostName(curr).equals(startHost)
//                        || visited.contains(curr)){
//                    continue;
//                }
//                visited.add(curr);
//                taskQueue.add(executor.submit(()->{
//                    List<String> nextUrls = htmlParser.getUrls(curr);
//                    for(String next: nextUrls){
//                        queue.offer(next);
//                    }
//                }));
//            }else{
//                if(taskQueue.isEmpty()){
//                    executor.shutdown();
//                    break;
//                }else{
//                    Future nextTask = taskQueue.poll();
//                    try{
//                        nextTask.get();
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>(visited);
//    }
//}

public class ImplementWebCrawler {
}
