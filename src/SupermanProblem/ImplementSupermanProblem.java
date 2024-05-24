package SupermanProblem;

//PROBLEM STATEMENT//

//You are designing a library of superheroes for a video game that your fellow developers will consume.
// Your library should always create a single instance of any of the superheroes and return the same
// instance to all the requesting consumers.

//Say, you start with the class Superman. Your task is to make sure
// that other developers using your class can never instantiate multiple copies of superman.
// After all, there is only one superman!

// the problem essentially is to create a singleton class !!

//1. -> make the constructor private, 2 -> create a public method getInstance()
// example or Double checked singleton method

class SuperMan{
    private static volatile SuperMan superMan= null;// volatile bcz constructor is not atomic
    private SuperMan(){}
    public static SuperMan getInstance(){

        if(superMan == null){ // this bcz we don't want resource to be wasted to get synchronized everytime!
            synchronized (SuperMan.class){
                if(superMan==null){
                    superMan = new SuperMan();
                }
            }
        }

        return superMan;
    }
}



public class ImplementSupermanProblem {
    public static void main(String[] args){

    }
}
