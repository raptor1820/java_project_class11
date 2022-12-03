import java.util.Scanner;
import java.io.*;

public class index {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 if you are a Teacher or 2 if you are a student");
        int choice = sc.nextInt();
        sc.nextLine(); // https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
        if (choice == 1) {
            // teacher login
            boolean auth = false;
            while (!auth) {
                System.out.println("------------------------------------------------------------------");
                System.out.println(
                        "Enter teacher username & password\nenter \"exit\" (without quotes) in both username and password to exit");
                System.out.println("Enter username");
                String username = sc.nextLine();
                System.out.println("Enter password");
                String password = sc.nextLine();
                System.out.println("------------------------------------------------------------------");
                if (username.equals("exit") && password.equals("exit")) {
                    break;
                }
                FileReader fr = new FileReader("auth.txt");
                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null) {
                    String arr[] = s.split(",");
                    if (username.equals(arr[0]) && password.equals(arr[1])) {
                        auth = true;
                        break;
                    }
                    System.out.println("Invalid username or password");
                }
                br.close();
            }
            if (!auth) {
                System.out.println("Teacher not verified. Terminating program.");
                System.exit(-1); // terminate program with an error
            }
            int menuOption = -1;
            while (menuOption != 5) {

                System.out.println(
                        "1. View the Question Bank\n2. View Questions with Answers\n3. Update the Question Bank\n4. View Scores\n5. Exit\n");
                String menuOptionchar = sc.nextLine();
                // try catch to prevent any input other than integer
                try {
                    menuOption = Integer.parseInt(menuOptionchar);
                } catch (Exception e) {
                    System.out.println("Invalid option.");
                    continue; // starts the loop again
                }

                if (menuOption == 1) {
                    // view question bank
                    System.out.println("QUESTION BANK");
                    FileReader fr = new FileReader("questions.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String arr[] = line.split("`");
                        System.out.println(arr[0]);
                    }
                    System.out.println("\f");
                    br.close();
                } else if (menuOption == 2) {
                    // view question bank with answerd
                    System.out.println("QUESTIONS WITH ANSWERS");
                    FileReader fr = new FileReader("questions.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String arr[] = line.split("`");
                        System.out.println("Question: " + arr[0]);
                        System.out.println("Option A: " + arr[1]);
                        System.out.println("Option B: " + arr[2]);
                        System.out.println("Option C: " + arr[3]);
                        System.out.println("Option D: " + arr[4]);
                        System.out.println("Correct Answer: " + arr[5] + "\n");
                    }
                    br.close();
                } else if (menuOption == 3) {
                    // update questions
                    char curr_optn = 'y';
                    System.out.println("UPDATE QUESTIONS");

                    FileWriter fw = new FileWriter("questions.txt", true);
                    PrintWriter update = new PrintWriter(fw);
                    while (!(curr_optn == 'n')) {
                        System.out.println("------------------------------------------------------------------");
                        System.out.println("Enter the question");
                        String question = sc.nextLine();
                        System.out.println("Enter option A");
                        String a = sc.nextLine();
                        System.out.println("Enter option B");
                        String b = sc.nextLine();
                        System.out.println("Enter option C");
                        String c = sc.nextLine();
                        System.out.println("Enter option D");
                        String d = sc.nextLine();
                        System.out.println("Enter the correct answer");
                        boolean flag = true;
                        char correct = '\u0000'; // null
                        while (flag) {
                            correct = Character.toLowerCase(sc.nextLine().charAt(0));
                            if (correct == 'a' || correct == 'b' || correct == 'c' || correct == 'd') {
                                flag = false;
                                break;
                            } else {
                                System.out.println("Invalid option. Enter again");
                            }
                        }
                        String built_str = question + "`" + a + "`" + b + "`" + c + "`" + d + "`" + correct;
                        update.println(built_str);
                        System.out.println("continue? (y/n)");
                        curr_optn = sc.nextLine().charAt(0);
                        System.out.println("------------------------------------------------------------------");
                    }
                    update.close();
                } else if (menuOption == 4) {
                    // view scores
                    System.out.println("SCORES");
                    FileReader fr = new FileReader("scores.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        String arr[] = line.split(",");
                        System.out.println("Name: " + arr[0] + " Score: " + arr[1]
                                + "\n");
                    }
                    br.close();
                } else if (menuOption == 5) {
                    break;
                } else {
                    System.out.println("Invalid choice");
                }

            }

        } else if (choice == 2) {
            // playing the quiz as a student
            System.out.println("Enter your name");
            String name = sc.nextLine();
            FileReader fr = new FileReader("questions.txt");
            Scanner file = new Scanner(fr);
            int count = 0;
            while (file.hasNextLine()) { // finding number of questions
                count++;
                file.nextLine();
            }
            file.close();
            String arr[] = new String[count];
            FileReader fr1 = new FileReader("questions.txt");
            BufferedReader br = new BufferedReader(fr1);
            String line;
            count = 0;
            while (!((line = br.readLine()) == null)) {
                arr[count] = line;
                count++;
            }
            System.out.println(count);
            // generate an array with 10 random index numbers
            int rand_arr[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }; // 10 -1s because -1 will never be an array
                                                                         // index
            for (int i = 0; i < 10; i++) {
                while (rand_arr[i] == -1) {
                    int randInt = (int) (Math.random() * (count - 1));
                    if (!(linearSearch(rand_arr, randInt))) {
                        rand_arr[i] = randInt;
                    }
                }
            }
            // char array to store user answers
            char answers[] = new char[10];
            // char array for correct answers
            char corr_ans[] = new char[10];
            for (int i = 0; i < rand_arr.length; i++) {
                boolean flag = true;
                char ans = '\u0000';
                System.out.println("------------------------------------------------------------------");
                while (flag) {
                    String s = arr[rand_arr[i]];
                    String array[] = s.split("`");
                    corr_ans[i] = array[5].charAt(0);
                    System.out.println("QUESTION: " + array[0]);
                    System.out.println("A. " + array[1]);
                    System.out.println("B. " + array[2]);
                    System.out.println("C. " + array[3]);
                    System.out.println("D. " + array[4]);
                    System.out.println("Enter your choice:");
                    ans = Character.toLowerCase(sc.nextLine().charAt(0));
                    if (ans == 'a' || ans == 'b' || ans == 'c' || ans == 'd') {
                        flag = false;
                    } else {
                        System.out.println("Invalid Choice. Enter again");
                    }
                }
                System.out.println("------------------------------------------------------------------");
                answers[i] = Character.toLowerCase(ans);

            }
            int score = 0;
            System.out.println("######################################################################");
            System.out.println("                                SOLUTIONS");
            System.out.println("######################################################################");
            for (int i = 0; i < rand_arr.length; i++) {

                String s = arr[rand_arr[i]];
                String array[] = s.split("`");
                System.out.println("------------------------------------------------------------------");
                if (answers[i] == corr_ans[i]) {
                    score++;
                    System.out.println("QUESTION: " + array[0]);
                    System.out.println("A. " + array[1]);
                    System.out.println("B. " + array[2]);
                    System.out.println("C. " + array[3]);
                    System.out.println("D. " + array[4]);
                    System.out.println("Your answer " + answers[i] + " (CORRECT)\n");
                } else {
                    System.out.println("QUESTION: " + array[0]);
                    System.out.println("A. " + array[1]);
                    System.out.println("B. " + array[2]);
                    System.out.println("C. " + array[3]);
                    System.out.println("D. " + array[4]);
                    System.out.println(
                            "Your answer: " + answers[i] + " (INCORRECT) Correct answer: " + corr_ans[i] + "\n");
                }
            }
            System.out.println("------------------------------------------------------------------");
            System.out.println("Your score is: " + (score * 10));
            FileWriter fw = new FileWriter("scores.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(name + "," + (score * 10));
            pw.close();
            br.close();
        }
        sc.close();
    }

    public static boolean linearSearch(int arr[], int key) { // search for elements in the array
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }
}
