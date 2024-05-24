package H2OMoleculeGenertor;

import java.util.Collections;
import java.util.Arrays;

class H2OMachine {
    Object sync;
    String[] molecule;
    int count;

    public H2OMachine() {
        molecule = new String[3];
        count=0;
        sync = new Object();
    }

    public void HydrogenAtom() {
        synchronized (sync){
            while(Collections.frequency(Arrays.asList(molecule),"H")==2){
                try{
                    sync.wait();
                }catch (Exception e){
                }
            }
            molecule[count] = "H";
            count++;

            if(count==3){
                for(String element: molecule){
                    System.out.print(element);
                }
                Arrays.fill(molecule,null);
                count = 0;
            }
            sync.notifyAll();
        }

    }

    public void OxygenAtom() {
        synchronized (sync){
            while(Collections.frequency(Arrays.asList(molecule),"O")==1){
                try{
                    sync.wait();
                }catch (Exception e){
                }
            }

            molecule[count]="O";
            count++;


            if(count==3){
                for (String element: molecule) {
                    System.out.print(element);
                }
                Arrays.fill(molecule,null);
                count = 0;
            }
            sync.notifyAll();

        }

    }
}

class H2OMachineThread extends Thread {
    private H2OMachine molecule;
    private String atom;

    public H2OMachineThread(H2OMachine molecule, String atom) {
        this.molecule = molecule;
        this.atom = atom;
    }

    public void run(){
        if("H".equals(atom)){
            try{
                molecule.HydrogenAtom();
            }catch (Exception e){
            }
        }else if("O".equals(atom)){
            try {
                molecule.OxygenAtom();
            }catch(Exception e){
            }
        }
    }
}

//Thread tj = new Thread(()-> fn())

public class MoleculeGenerator {
    public static void main(String[] args){
        H2OMachine molecule = new H2OMachine();

        Thread t1 = new H2OMachineThread(molecule,"O");
        Thread t2 = new H2OMachineThread(molecule,"O");
        Thread t3 = new H2OMachineThread(molecule,"O");
        Thread t4 = new H2OMachineThread(molecule,"O");
        Thread t5 = new H2OMachineThread(molecule,"H");
        Thread t6 = new H2OMachineThread(molecule,"H");

        t2.start();
        t6.start();
        t1.start();
        t4.start();
        t3.start();
        t5.start();
    }
}
