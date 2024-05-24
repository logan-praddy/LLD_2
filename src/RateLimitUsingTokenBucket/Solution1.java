package RateLimitUsingTokenBucket;

class TokenFilter{
    private int maxTokens;
    private long possibleTokens = 0;
    private long lastRequestTime = System.currentTimeMillis();

    public TokenFilter(int maxTokens){
        this.maxTokens = maxTokens;
    }

    synchronized void getTokens() throws InterruptedException{
        possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;
        if(possibleTokens>maxTokens){
            possibleTokens=maxTokens;
        }
        if(possibleTokens>0){
            possibleTokens--;
        }else{
            Thread.sleep(1000); // since we need one token to be generated
        }
        lastRequestTime = System.currentTimeMillis();
        System.out.println(
                "Granting " + Thread.currentThread().getName() + " token at " + (System.currentTimeMillis() / 1000));
    }

}
public class Solution1 {
    public static void main(String[] args){

    }
}
