import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.concurrent.*;

public class notTerms{
    public static void main(String[] args){
        ArrayList<String> linesFromFile = getLinesFromFile("pythonGlossary.csv");  
        HashMap<String,ArrayList<String>> ctd = chapterTermDict(linesFromFile); 
        HashMap<String,ArrayList<String>> dtd = definitionTermDict(linesFromFile);
        // System.out.println(ctd.get("1"));
        // System.out.println(dtd.get("method"));
        boolean studying = true;
        int numCorrectAnswers = 0;
        int numQuestionsAsked = 0;
        System.out.println("Welcome to the Vocabulary Drill App. This program will supply questions, asking which term fits the provided definition. To answer, please supply the term in its entirety. ");
        System.out.println("How many questions would you like?");
        int numQuestions = -1;
        while(numQuestions == -1){
            try{
                Scanner s = new Scanner(System.in);
                numQuestions = Integer.parseInt(s.nextLine());
            }catch(Exception e){
                System.out.println("Enter a valid number of questions: ");
            }
        }
        System.out.println("What chapter would you like to study? Enter a number for any chapter 1-15: ");
        while(numQuestionsAsked< numQuestions){
            boolean answeredCorrectly = true;
            answeredCorrectly = askQuestions(ctd,dtd);
            if(answeredCorrectly){
                numCorrectAnswers++;
            }
            numQuestionsAsked++;  
            System.out.println("Your score is "+numCorrectAnswers+"/"+numQuestionsAsked);          
        
        }
        System.out.println("Out of "+numQuestions+" questions you correctly answered "+numCorrectAnswers+ " correctly. Thank you for using the Vocabulary Drill App! ");

    }//main

    static ArrayList<String> getLinesFromFile(String filename){
        ArrayList<String> linesFromFile = new ArrayList<>();
        try{
            Scanner s = new Scanner(new File(filename));
            s.nextLine();
            while(s.hasNextLine()){
                linesFromFile.add(s.nextLine());
            }
            return linesFromFile;
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return linesFromFile;
    }
    static HashMap<String, ArrayList<String>> chapterTermDict(ArrayList<String> linesFromFile){
        HashMap<String, ArrayList<String>> ctdToReturn = new HashMap<>();
        for(String line : linesFromFile){
            String[] splitLine = line.split(",");
            Collection<ArrayList<String>> ctdValues = ctdToReturn.values();
            ArrayList<ArrayList<String>> valuesList =  new ArrayList<>(ctdValues);
            ArrayList<String> placeholder = new ArrayList<>();
            valuesList.add(0,placeholder);
            ArrayList<String> terms = new ArrayList<>(valuesList.get(valuesList.size()-1));
            if(ctdToReturn.keySet().contains(splitLine[0])){
                terms.add(splitLine[1].trim());
                ctdToReturn.put(splitLine[0],terms);
            }else{
                terms.add(splitLine[1].trim());            
                ctdToReturn.put(splitLine[0].trim(),terms);
            }                
        }
        return ctdToReturn;
    }
    static HashMap<String, ArrayList<String>> definitionTermDict(ArrayList<String> linesFromFile){
        HashMap<String, ArrayList<String>> dtdToReturn = new HashMap<>();
        for(String line : linesFromFile){
            String[] splitLine = line.split(",");
            Collection<ArrayList<String>> dtdValues = dtdToReturn.values();
            ArrayList<ArrayList<String>> valuesList =  new ArrayList<>(dtdValues);
            ArrayList<String> placeholder = new ArrayList<>();
            valuesList.add(0,placeholder);
            ArrayList<String> definition = new ArrayList<>(valuesList.get(valuesList.size()-1));
            definition.clear();
            if(dtdToReturn.keySet().contains(splitLine[0])){
                definition.add(0,splitLine[2].trim());
                dtdToReturn.put(splitLine[1],definition);
            }else{
                definition.add(0,splitLine[2].trim());
                dtdToReturn.put(splitLine[1].trim(),definition);
            }                
        }
        return dtdToReturn; 
    }
    static boolean askQuestions(HashMap<String,ArrayList<String>> ctd , HashMap<String,ArrayList<String>> dtd){
        try{
            Random r = new Random();
            Scanner s = new Scanner(System.in);
            String chapter = s.nextLine();
            ArrayList<String> terms = ctd.get(String.valueOf(chapter));
            int termNum = r.nextInt(terms.size());
            String correct = terms.get(termNum);
            System.out.println(correct);
            String[] answers =  new String[4];
            for(int i = 0 ; i<4 ; i++){
                answers[i] = terms.get(r.nextInt(terms.size()));
            }
            answers[3] = correct;
            ArrayList<String> definition =  dtd.get(correct);  
            String def = definition.get(0);         
            System.out.println(def);    
            System.out.println("Which of the following means \""+ def +"\" ?");
            shuffleArray(answers);
            System.out.println("a. " +answers[0]);
            System.out.println("b. " +answers[1]);
            System.out.println("c. " +answers[2]);
            System.out.println("d. " +answers[3]);
            String userAnswer = s.nextLine();
            if(userAnswer.trim().toLowerCase().equals("quit")){
                System.out.println("Thank you for using the Vocabulary Drill App! ");
                // s.close();
                System.exit(0);
                return false;
            } 
            else if(userAnswer.trim().toLowerCase().equals(correct.toLowerCase())){
                System.out.println("Good job! \""+correct+"\" is the correct answer.");
                // s.close();
                return true;
            }else{
                System.out.println("That's incorrect. The correct answer was \""+correct+"\"");
                // s.close();
                return false;
            }
            
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
        return true;
    }
    // static String getChapter(){
    //     String toReturn = "";
    //     try{
    //         Scanner s = new Scanner(System.in);
    //         String input = s.nextLine();
    //         if(Integer.parseInt(input)<16 && Integer.parseInt(input)>0){
    //             return input;
    //         }
    //         else{
    //             System.out.println("Enter a valid chapter from 1-15: ");
    //         }
    //     }catch(Exception e){
    //         e.printStackTrace(System.out);
    //     }
    //     return toReturn;
    // }
    static void shuffleArray(String[] ar)  {
    // If running on Java 6 or older, use `new Random()` on RHS here
    Random rnd = ThreadLocalRandom.current();
    for (int i = ar.length - 1; i > 0; i--)
    {
      int index = rnd.nextInt(i + 1);
      // Simple swap
      String a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
  }
}//class