package Algorithm;

import AIS.Antibody;
import AIS.Antigen;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataSet {

    ArrayList<Antigen> antigenList;
    public Antigen[] testSet;
    public Antigen[] trainingSet;
    public double trainingTestSplit;
    private Random random;
    public HashMap<String, ArrayList<Antigen>> antigenMap;
    public DataSet(String path, double trainingTestSplit){
        this.trainingTestSplit = trainingTestSplit;
        this.random = new Random();
        readFile(path);
    }

    private void readFile(String path) {
        antigenList = new ArrayList<>();

        try (
                Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(this::processLine);

            //iterate over all the lines in the dataset
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.testSet = new Antigen[(int)(antigenList.size()*trainingTestSplit)];
        this.trainingSet = new Antigen[antigenList.size() - testSet.length];

        for(int i=0; i< trainingSet.length;i++){
            trainingSet[i] = antigenList.remove(random.nextInt(antigenList.size()));
        }
        this.testSet = antigenList.toArray(testSet);

        this.antigenMap = Antigen.createAntigenMap(trainingSet);
    }

    private void processLine(String line){

        var list = Arrays.asList(line.split(","));
        if(list.size() == 1){
            return;
        }

        double[] attributes = new double[list.size()-1];
        var label = "";

        for(int i=0;i<list.size();i++){
            if(i == list.size()-1){
                label = list.get(i);
            }
            else{
                attributes[i] = Double.parseDouble(list.get(i));
            }
        }
        Antigen antigen = new Antigen(attributes,label);

        antigenList.add(antigen);
    }
 }
