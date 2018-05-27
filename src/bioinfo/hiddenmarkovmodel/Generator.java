package bioinfo.hiddenmarkovmodel;


import java.util.Random;

public class Generator {
    private HMM hmm;

    private String sequence;
    private String states;

    public Generator(HMM hmm) {
        this.hmm = hmm;
    }

    public String generateSequence() {

        //reset sequence of symbols and states
        sequence = "";
        states = "";


        int currentState = 0;
        char currentSymbol = '#'; //this is the currentsymbol which should be deleted

        currentState = getNextState(currentState);

        while(currentState != 0) {

            currentSymbol = getNextSymbol(currentState);

            sequence = sequence + currentSymbol;
            states = states +  hmm.getStateName(currentState);

            currentState = getNextState(currentState);
        }


        return sequence;
    }

    //choose randomly the following state starting from the given currentState
    //
    private int getNextState(int currentState) {
        double rand = Math.random();
        int nextState = 0; //starting at the first possible state
        double nextTransprob = hmm.getTransMatrix(currentState,nextState);

        //this implementation is not 100% stable, because the added probalities of the transition states could be less than 1
        while(rand > nextTransprob) {
            nextState++;
            nextTransprob = nextTransprob + hmm.getTransMatrix(currentState, nextState);
        }

        return nextState;
    }

    //choose randomly a symbol for a given state
    private char getNextSymbol(int currentState) {
        double rand = Math.random();

        int nextSymbol = 0; //starting at the first possible state
        double nextTransprob = hmm.getEmissionProb(currentState,nextSymbol);

        //this implementation is not 100% stable, because the added probalities of the transition states could be less than 1
        while(rand > nextTransprob) {
            nextSymbol++;
            try {
                nextTransprob = nextTransprob + hmm.getEmissionProb(currentState, nextSymbol);

            }catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println(rand);
                nextSymbol = 0;
            }
        }

        return hmm.getSymbolName(nextSymbol);
    }

    public String getStates() {
        return this.states;
    }

    public String getSequence() {
        return this.sequence;
    }
}
