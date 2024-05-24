package RateLimitUsingTokenBucket;

public final class TokenBucketFilterFactory {
    private TokenBucketFilterFactory(){

    }

    static public TokenBucketFilter makeTokenBucketFilter(int capacity){
        MultithreadedTokenBucketFilter multithreadedTokenBucketFilter = new MultithreadedTokenBucketFilter(capacity);
        multithreadedTokenBucketFilter.initialize();
        return multithreadedTokenBucketFilter;
    }

    private static class MultithreadedTokenBucketFilter extends TokenBucketFilter{
        private long possibleTokens = 0;
        private final int MAX_TOKENS;
        private final int ONE_SECOND = 1000;

        MultithreadedTokenBucketFilter(int capacity){
            this.MAX_TOKENS = capacity;
        }

        void initialize(){
            Thread dt = new Thread(()-> {
                daemonThread();
            });
            dt.setDaemon(true);
            dt.start();
        }

        private void daemonThread(){
            while(true){
                synchronized (this){
                    if(possibleTokens < MAX_TOKENS){
                        possibleTokens++;
                    }
                    this.notify();
                }
                try{
                    Thread.sleep(ONE_SECOND);
                }catch (InterruptedException e){

                }
            }
        }

        public void getToken() throws InterruptedException{
            synchronized (this){
                while(possibleTokens==0){
                    this.wait();
                }
                possibleTokens--;
            }
            System.out.println(
                    "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);

        }
    }
}
