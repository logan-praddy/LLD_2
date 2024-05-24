package AsyncToSyncNetflix;

// we have an executor which is async, we need to make it sync without changing its code

// solution create a wrapper around executor -> SynchronousExecutor
// now in its execution func, use object to wait and notify after wrapping the original
// callback function which eventually solves the problem!

class ImplementAsyncToSync {
    public static void main( String args[] ) throws Exception {
        SynchronousExecutor executor = new SynchronousExecutor();
        executor.asynchronousExecution(() -> {
            System.out.println("I am done");
        });

        System.out.println("main thread exiting...");
    }
}

interface Callback {

    public void done();
}


class SynchronousExecutor extends Executor {

    @Override
    public void asynchronousExecution(Callback callback) throws Exception {

        Object signal = new Object();
        final boolean[] isDone = new boolean[1];

        Callback cb = new Callback() {

            @Override
            public void done() {
                callback.done();
                synchronized (signal) {
                    signal.notify();
                    isDone[0] = true;
                }
            }
        };

        // Call the asynchronous executor
        super.asynchronousExecution(cb);

        synchronized (signal) {
            while (!isDone[0]) {
                signal.wait();
            }
        }

    }
}

class Executor {

    public void asynchronousExecution(Callback callback) throws Exception {

        Thread t = new Thread(() -> {
            // Do some useful work
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
            }
            callback.done();
        });
        t.start();
    }
}
