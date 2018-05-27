package bioinfo.hiddenmarkovmodel;


public class Evaluator {

    public static void evaluateHMM(HMM hmm) {
        Generator generator = new Generator(hmm);
        Decoder decoder = new Decoder(hmm);

        for (int i = 0; i < 100; i++) {
            generator.generateSequence();
            decoder.decodeSequence(generator.getSequence());

            int failsU = 0;
            int failsF = 0;
            for (int j = 0; j < decoder.getDecodedStates().length(); j++) {
                if(decoder.getDecodedStates().charAt(j) == generator.getStates().charAt(j)) {
                    if (generator.getStates().charAt(j) == 'U') {
                        failsU++;
                    } else {
                        failsF++;
                    }
                }
            }

            String failReport = "Length of Sequence: "+ generator.getStates().length() +
                    "\t Fails for U:  " + failsU +
                    "\t Fails for F:  " + failsF +
                    "\t Average failrate: " +  (0.0 + failsF+failsU) /generator.getStates().length();

            System.out.println(failReport);
        }
    }
}
