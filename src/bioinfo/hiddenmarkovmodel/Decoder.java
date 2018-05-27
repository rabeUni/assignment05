package bioinfo.hiddenmarkovmodel;

public class Decoder {
    private HMM hmm;
    private String sequence;
    private String decodedStates;

    public Decoder(HMM hmm) {
        this.hmm = hmm;
    }

    public String decodeSequence(String sequence) {
        try {
            sequence = sequence;
            decodedStates = this.hmm.runViterbi(sequence);
            return decodedStates;

        } catch (Exception ex) {
            System.out.println("Fail during running vertibi algorithm: "+ ex.toString());
            return "Fail during running vertibi algorithm";
        }
    }

    public String getSequence(){
        return this.sequence;
    }

    public String getDecodedStates(){
        return this.decodedStates;
    }
}
