package bioinfo.hiddenmarkovmodel;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * A simple HMM class
 * YOUR_NAME_HERE_PLEASE
 * GBi, 2018, Daniel Huson
 */
public class HMM {
    // 0. Private variables:
    // The HMM:
    private int numStates;
    private char stateNames[];
    private int numSymbols;
    private char symbolNames[];
    private double transMatrix[][];
    private double emissionProb[][];

    // I. Constructors:

    /**
     * Construct an empty HMM.
     */
    public HMM() {
        this(0, 0);
    }

    /**
     * Construct an empty HMM.
     *
     * @param numStates  the number of states that the HMM will hold
     * @param numSymbols the number of symbols that the HMM will hold
     */
    public HMM(int numStates, int numSymbols) {
        this.numStates = numStates;
        this.stateNames = new char[numStates];
        this.numSymbols = numSymbols;
        this.symbolNames = new char[numSymbols];
        this.transMatrix = new double[numStates][numStates];
        this.emissionProb = new double[numStates][numSymbols];
    }

    /**
     * Construct an HMM
     *
     * @param numStates    the number of states that the HMM will hold
     * @param stateNames   the names of the states
     * @param numSymbols   the number of symbols that the HMM will hold
     * @param symbolNames  the symbols
     * @param transMatrix  the transition matrix
     * @param emissionProb the emission probabilities
     */
    public HMM(int numStates, char stateNames[], int numSymbols, char symbolNames[], double transMatrix[][], double emissionProb[][]) {
        this.numStates = numStates;
        this.stateNames = stateNames;
        this.numSymbols = numSymbols;
        this.symbolNames = symbolNames;
        this.transMatrix = transMatrix;
        this.emissionProb = emissionProb;
    }

    /**
     * Construct an HMM from a Reader
     *
     * @param r the Reader
     */
    public HMM(Reader r) throws Exception {
        this(0, 0);
        StreamTokenizer st = new StreamTokenizer(r);
        st.commentChar('#');
        st.eolIsSignificant(false);
        st.parseNumbers();
        st.slashSlashComments(false);
        st.slashStarComments(false);
        st.wordChars(42, 43);

        try {
            // read number of states
            if (st.nextToken() != StreamTokenizer.TT_EOF && st.ttype == StreamTokenizer.TT_NUMBER && st.nval > 0)
                numStates = (int) st.nval;
            else throw new
                    Exception("Parse Error in line: " + st.lineno());

            // read the names of the states:
            stateNames = new char[numStates];
            for (int i = 0; i < numStates; i++) {
                if (st.nextToken() != StreamTokenizer.TT_EOF) {
                    if (st.ttype == StreamTokenizer.TT_WORD)
                        stateNames[i] = st.sval.charAt(0);
                    else if (st.ttype == StreamTokenizer.TT_NUMBER)
                        stateNames[i] = (new Double(st.nval)).toString().charAt(0);
                } else
                    throw new
                            Exception("Parse Error in line: " + st.lineno());
            }


            // read number of symbols
            if (st.nextToken() != StreamTokenizer.TT_EOF && st.ttype == StreamTokenizer.TT_NUMBER && st.nval > 0)
                numSymbols = (int) st.nval;
            else throw new
                    Exception("Parse Error in line: " + st.lineno());


            // read the names of the symbols:
            symbolNames = new char[numSymbols];
            for (int i = 0; i < numSymbols; i++) {
                if (st.nextToken() != StreamTokenizer.TT_EOF) {
                    if (st.ttype == StreamTokenizer.TT_WORD)
                        symbolNames[i] = st.sval.charAt(0);
                    else if (st.ttype == StreamTokenizer.TT_NUMBER)
                        symbolNames[i] = (new Double(st.nval)).toString().charAt(0);
                } else
                    throw new
                            Exception("Parse Error in line: " + st.lineno());
            }
            // read transition matrix:
            transMatrix = new double[numStates][numStates];
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    if (st.nextToken() != StreamTokenizer.TT_EOF && st.ttype == StreamTokenizer.TT_NUMBER)
                        transMatrix[i][j] = st.nval;
                    else throw new
                            Exception("Parse Error in line: " + st.lineno());
                }
            }
            // read emission probabilities:
            emissionProb = new double[numStates][numSymbols];
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numSymbols; j++) {
                    if (st.nextToken() != StreamTokenizer.TT_EOF && st.ttype == StreamTokenizer.TT_NUMBER)
                        emissionProb[i][j] = st.nval;
                    else throw new
                            Exception("Parse Error in line: " + st.lineno());
                }
            }
            checkValid();
        } catch (java.io.IOException ex) {
            throw new IOException("Parse error in line " + st.lineno() + ": " + ex.getMessage());
        }
    }

    // II. Input and output of HMMs:

    /**
     * Read an HMM from a string.
     *
     * @param str the string containing a description of the HMM
     */
    public static HMM valueOf(String str) throws Exception {
        return new HMM(new StringReader(str));
    }

    /**
     * Print a description of the HMM to a string.
     *
     * @return a description of the HMM
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("#####\n");
        str.append("# Number of states:\n ").append(getNumStates()).append("\n");
        str.append("# States:\n");
        for (int i = 0; i < getNumStates(); i++)
            str.append(" ").append(stateNames[i]);
        str.append("\n");
        str.append("# Number of symbols:\n ").append(getNumSymbols()).append("\n");
        str.append("# Symbols:\n");
        for (int i = 0; i < getNumSymbols(); i++)
            str.append(" ").append(symbolNames[i]);
        str.append("\n");
        str.append("# Transition matrix:\n");
        for (int i = 0; i < getNumStates(); i++) {
            for (int j = 0; j < getNumStates(); j++)
                str.append(" ").append(getTransMatrix(i, j));
            str.append("\n");
        }
        str.append("# Emission probabilities:\n");
        for (int i = 0; i < getNumStates(); i++) {
            for (int j = 0; j < getNumSymbols(); j++)
                str.append(" ").append(getEmissionProb(i, j));
            str.append("\n");
        }
        str.append("#####\n");
        return str.toString();
    }

    // III. Methods for querying the HMM (is and get functions):

    /**
     * Check that the HMM contains valid and consistent data, throw an
     * exception, if this is not the case
     */
    public void checkValid() throws Exception {
        if (numStates < 0 || numSymbols < 0)
            throw new Exception("Invalid data, numStates="
                    + numStates + ", numSymbols=" + numSymbols);
        // Test transMatrix:
        for (int i = 0; i < numStates; i++) {
            double sum = 0;
            for (int j = 0; j < numStates; j++) {
                if (transMatrix[i][j] < 0)
                    throw new Exception("Invalid data, transMatrix["
                            + i + "][" + j + "]=" + transMatrix[i][j]);
                sum += transMatrix[i][j];
            }
            if (Math.abs(1.0 - sum) > 0.000001)
                throw new Exception("Invalid data, transMatrix(row="
                        + i + "): " + sum);
        }
        // (No need to check begin and end state...)

        // test emission probabilities:
    }

    /**
     * Get the number of states
     *
     * @return number of states
     */
    public int getNumStates() {
        return numStates;
    }

    /**
     * Get the name of the i-th state
     *
     * @param i the rank of the state
     * @return the name of the i-th state
     */
    public char getStateName(int i) {
        return stateNames[i];
    }

    /**
     * Get the number of symbols
     *
     * @return number of symbols
     */
    public int getNumSymbols() {
        return numSymbols;
    }

    /**
     * Get the name of the i-th symbol
     *
     * @param i the rank of the symbol
     * @return the name of the i-th symbol
     */
    public char getSymbolName(int i) {
        // Careful here: emitSymbol will return -1, if called while
        // in the begin/end state 0. Need to make sure you don't call
        // getSymbolName then!
        return symbolNames[i];
    }

    /**
     * Get the rank i of a symbol s_i
     *
     * @param s the symbol
     * @return the rank 0..numSymbols-1 of the symbol
     */
    public int getSymbolRank(char s) throws Exception {
        for (int i = 0; i < getNumSymbols(); i++)
            if (s == getSymbolName(i))
                return i;
        throw new Exception("Unrecognized symbol: " + s);
    }

    /**
     * Get the transition probability for s to t
     *
     * @param s the source state
     * @param t the target state
     * @return transition probability s to t
     */
    public double getTransMatrix(int s, int t) {
        return transMatrix[s][t];
    }

    /**
     * Get the emission probability for state k and symbol s
     *
     * @param k the state
     * @param s the symbol state
     * @return emission probability for state k and symbol s
     */
    public double getEmissionProb(int k, int s) {
        return emissionProb[k][s];
    }

	/* IV. Methods for setting data: */

    /**
     * Set the i-th symbol
     *
     * @param i the rank of the symbol
     * @param s the symbol
     */
    public void setSymbol(int i, char s) {
        symbolNames[i] = s;
    }

    // The preceding is just an example of a set method. This should
    // be followed by more set methods, which we haven't got here because we
    // don't need them yet...


    /**
     * The Viterbi algorithm is run to decode the given sequence of
     * symbols.
     *
     * @param seq the sequence of observed symbols
     * @return the most probable sequence of states
     */
    public String runViterbi(String seq) throws Exception {
        // *** Algorithm as described in the lecture and in DEKM98, page. 56:

        // setup arrays:
        int L = seq.length();
        double v[][] = new double[numStates][L + 1]; // Viterbi variable
        int ptr[][] = new int[L + 1][numStates]; // traceback pointers
        int pi[] = new int[L + 1]; // most probable path

        // Initialization:
        v[0][0] = 1;
        for (int k = 1; k < numStates; k++)
            v[k][0] = 0;

        // Recursion:
        for (int i = 1; i <= L; i++) {
            // xi: Rank 0..numSymbols of current symbol
            int xi = getSymbolRank(seq.charAt(i - 1));

            for (int l = 0; l < numStates; l++) {
                //determine maximum of (v_k(i-1) a_kl)
                double maximum = v[0][i-1] * this.getTransMatrix(0,l);

                int arg_max = 0;

                for (int k = 0; k < numStates; k++) {
                    double actualValue = v[k][i-1] * this.getTransMatrix(k,l);
                    if(maximum < actualValue) {
                        maximum = actualValue;
                        arg_max = k;
                    }
                }

                v[l][i] = this.getEmissionProb(l,xi) * maximum;
                ptr[i][l] = arg_max;


            }
        }

        // Termination:
        double maxValue = 0;
        int maxPtr = 0;
        for (int k = 0; k < numStates; k++) {
            double actualValue = v[k][L] * this.getTransMatrix(k,0);
            if(maxValue < actualValue) {
                maxValue = actualValue;
                maxPtr = k;
            }
        }

        double prob = maxValue; // probability of most probable path
        pi[L] = maxPtr;

        // Traceback:
        for (int i = L; i >= 1; i--)
            pi[i - 1] = ptr[i][pi[i]];

        // *** End of algorithm

        // Generate string of state names:
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= L; i++)
            sb.append(getStateName(pi[i]));
        String path = sb.toString();

        // Print out the probability computed:
        //System.out.println("#Viterbi P=" + prob);
        return path;
    }
}

// EOF
