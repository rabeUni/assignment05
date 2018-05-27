package bioinfo.hiddenmarkovmodel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {


    public static void main(String[] args) {

        HMM hmm = null;
        String filepath = "casino.hmm";

        try {
            FileReader fileReader = new FileReader(filepath);

            BufferedReader reader = new BufferedReader(fileReader);

            hmm = new HMM(reader);

        } catch (Exception ex) {
            System.out.println(filepath + "File not found."+ "or other fail: "+ ex.toString());
        }

        Generator generator = new Generator(hmm);

        System.out.println(generator.generateSequence());
        System.out.println(generator.getStates());

        Decoder decoder = new Decoder(hmm);

        System.out.println(decoder.decodeSequence(generator.getSequence()));

        Evaluator.evaluateHMM(hmm);
    }
}
