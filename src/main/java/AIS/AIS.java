package AIS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AIS {

    private Antigen[] antigens;
    private Antibody[] antibodies;
    private HashMap<String,ArrayList<Antigen>> antigenMap;
    private HashMap<String,double[][]> featureMap;
    private HashMap<String, ArrayList<Antibody>> antibodyMap;
    private Random random = new Random();
    private int populationSize;

    public AIS(Antigen[] antigens, HashMap<String,ArrayList<Antigen>> antigenMap,int populationSize){
        //this.antibodies = antibodies;
        this.antigens = antigens;
        this.antigenMap = antigenMap;
        this.featureMap = new HashMap<>();
        this.antibodyMap = new HashMap<>();
        this.populationSize = populationSize;
        this.antibodies = new Antibody[populationSize];
        initialisePopulation(this.populationSize);
    }

    public void iterate(){
        /*for (Antibody antibody:antibodies) {
            antibody.calculateFitness(antigens);
        }*/
        antibodies[0].calculateFitness(antigens);
    }


    public void initialisePopulation(int populationSize){
        int antibodyCount = 0;
        for(String label:antigenMap.keySet()){
            if(antibodyCount >= populationSize){
                break;
            }
            createFeatureMap(antigenMap.get(label));

            int labelCount = (int)(((double)this.antigenMap.get(label).size()/antigens.length)*populationSize);

            for (int i=0;i<labelCount;i++){
                Antibody antibody = createAntibody(label);
                if(this.antibodyMap.containsKey(label)){
                    this.antibodyMap.get(label).add(antibody);

                }else{
                    this.antibodyMap.put(label, new ArrayList<>(){ { add(antibody);}});
                }
                antibodies[antibodyCount] = antibody;
                antibodyCount++;
            }
        }

        if(antibodyCount < populationSize){
            Antigen radnomAntigen = antigens[random.nextInt(antigens.length)];
            String label = radnomAntigen.getLabel();

            Antibody antibody = createAntibody(label);
            antibodyMap.get(label).add(antibody);
            antibodies[populationSize-1] = antibody;

        }
    }

    public void createFeatureMap(ArrayList<Antigen> antigens){
        double[][] features = new double[antigens.get(0).getAttributes().length][2];

        for(double[] featureBound:features){
            featureBound[0] = Double.MAX_VALUE;
            featureBound[1] = Double.MIN_VALUE;
        }

        for (int j=0;j<antigens.size();j++){
            for(int i=0;i<antigens.get(j).getAttributes().length;i++){

                double featureValue = antigens.get(j).getAttributes()[i];

                if(featureValue < features[i][0]){
                    features[i][0] = featureValue;
                }else if(featureValue > features[i][1]){
                    features[i][1] = featureValue;
                }
            }
        }
            featureMap.put(antigens.get(0).getLabel(),features);
    }


    public Antibody createAntibody(String label){
        double[][] featureList = featureMap.get(label);
        double[] attributes = new double[featureList.length];

        double maxAverage = 0;
        double minAverage = 0;

        for(int i=0; i<featureMap.get(label).length;i++){
            double[] featureBounds = featureMap.get(label)[i];
            double maxValue = featureBounds[1]*1.1;
            double minValue = featureBounds[0]*0.9;

            maxAverage += maxValue;
            minAverage += minValue;

            attributes[i] = minValue + (maxValue - minValue)*random.nextDouble();
            //System.out.println("maxvalue "+ maxValue+" "+"minvalue "+minValue+"random in range "+attributes[i]);
        }

        minAverage = minAverage/featureMap.get(label).length;
        maxAverage = maxAverage/featureMap.get(label).length;

        //TODO: Make initial radius better
        double radius = (minAverage + (maxAverage - minAverage) * random.nextDouble())*0.4;

        return new Antibody(attributes, radius, label);

    }
    public Antigen[] getAntigens() {
        return antigens;
    }

    public void setAntigens(Antigen[] antigens) {
        this.antigens = antigens;
    }

    public Antibody[] getAntibodies() {
        return antibodies;
    }

    public void setAntibodies(Antibody[] antibodies) {
        this.antibodies = antibodies;
    }


}


