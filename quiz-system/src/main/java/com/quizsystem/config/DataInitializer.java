package com.quizsystem.config;

import com.quizsystem.model.*;
import com.quizsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data initializer that preloads the database with:
 * - 2 default users (admin + student)
 * - 4 quiz categories (DSA, Java, Python, ML)
 * - 8 quizzes (2 per category)
 * - 96 questions with 384 options
 *
 * Only runs if no users exist (first-time setup).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
                           QuizRepository quizRepository, QuestionRepository questionRepository,
                           OptionRepository optionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Only initialize if database is empty
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping seed data.");
            return;
        }

        System.out.println("=== Initializing Quiz System Data ===");

        // Create default users
        createUsers();

        // Create categories and quizzes with questions
        createDSACategory();
        createJavaCategory();
        createPythonCategory();
        createMLCategory();

        System.out.println("=== Data Initialization Complete ===");
        System.out.println("  Default users: admin/admin123, student/student123");
        System.out.println("  Categories: 4 | Quizzes: 8 | Questions: 96");
    }

    // ===== Users =====
    private void createUsers() {
        userRepository.save(new User("admin", passwordEncoder.encode("admin123"), Role.ADMIN));
        userRepository.save(new User("student", passwordEncoder.encode("student123"), Role.STUDENT));
        System.out.println("  Created default users");
    }

    // ===== Helper: Add Question with 4 Options =====
    private void addQuestion(Quiz quiz, String text, String a, String b, String c, String d, int correctIndex) {
        Question question = new Question(text, false, quiz);
        question = questionRepository.save(question);

        String[] opts = {a, b, c, d};
        for (int i = 0; i < 4; i++) {
            optionRepository.save(new Option(opts[i], i == correctIndex, question));
        }
    }

    // =================================================================================
    // DATA STRUCTURES & ALGORITHMS - 2 Quizzes, 12 Questions Each
    // =================================================================================
    private void createDSACategory() {
        Category cat = categoryRepository.save(
                new Category("Data Structures & Algorithms", "Arrays, Linked Lists, Stacks, Queues, Sorting & Trees", "bi-diagram-3", "#6366f1"));

        // Quiz 1: Arrays, Linked Lists, Stacks
        Quiz q1 = quizRepository.save(new Quiz("Arrays, Linked Lists & Stacks", "Test your knowledge on fundamental data structures", 15, cat));

        addQuestion(q1, "What is the time complexity of accessing an element in an array by index?",
                "O(1)", "O(n)", "O(log n)", "O(n²)", 0);
        addQuestion(q1, "Which data structure uses LIFO (Last In First Out) principle?",
                "Queue", "Stack", "Linked List", "Array", 1);
        addQuestion(q1, "What is the time complexity of inserting an element at the beginning of a singly linked list?",
                "O(1)", "O(n)", "O(log n)", "O(n²)", 0);
        addQuestion(q1, "What is the main disadvantage of arrays?",
                "Fast access", "Fixed size", "Sequential storage", "Random access", 1);
        addQuestion(q1, "In a singly linked list, each node contains:",
                "Only data", "Data and a pointer to next node", "Data and pointers to next and previous nodes", "Only a pointer", 1);
        addQuestion(q1, "What operation does push() perform on a stack?",
                "Removes top element", "Adds element on top", "Returns top element", "Checks if stack is empty", 1);
        addQuestion(q1, "What is the worst-case time complexity of searching an element in an unsorted array?",
                "O(1)", "O(log n)", "O(n)", "O(n log n)", 2);
        addQuestion(q1, "Which of the following applications uses a stack?",
                "CPU Scheduling", "Function call management", "Print queue", "Breadth-first search", 1);
        addQuestion(q1, "What is the space complexity of a linked list with n nodes?",
                "O(1)", "O(log n)", "O(n)", "O(n²)", 2);
        addQuestion(q1, "What is a sentinel node in a linked list?",
                "The last node", "A dummy node used as a boundary", "A node with maximum value", "The middle node", 1);
        addQuestion(q1, "Which operation cannot be performed efficiently using an array?",
                "Accessing by index", "Inserting in the middle", "Binary search on sorted data", "Reading sequentially", 1);
        addQuestion(q1, "What does peek() operation do in a stack?",
                "Removes top element", "Adds new element", "Returns top element without removing", "Checks if stack is full", 2);

        // Quiz 2: Queues, Sorting, Trees
        Quiz q2 = quizRepository.save(new Quiz("Queues, Sorting & Trees", "Advanced data structures and sorting algorithms", 15, cat));

        addQuestion(q2, "Which data structure uses FIFO (First In First Out) principle?",
                "Stack", "Queue", "Tree", "Graph", 1);
        addQuestion(q2, "What is the average time complexity of Quick Sort?",
                "O(n)", "O(n log n)", "O(n²)", "O(log n)", 1);
        addQuestion(q2, "In a Binary Search Tree (BST), the left child is:",
                "Greater than parent", "Less than parent", "Equal to parent", "Random", 1);
        addQuestion(q2, "What is the best-case time complexity of Bubble Sort?",
                "O(n²)", "O(n)", "O(n log n)", "O(1)", 1);
        addQuestion(q2, "A complete binary tree with n nodes has height:",
                "O(n)", "O(log n)", "O(n²)", "O(1)", 1);
        addQuestion(q2, "Which sorting algorithm is NOT comparison-based?",
                "Merge Sort", "Quick Sort", "Counting Sort", "Heap Sort", 2);
        addQuestion(q2, "What is a priority queue?",
                "A FIFO queue", "A queue where elements are dequeued by priority", "A stack variant", "A circular array", 1);
        addQuestion(q2, "The in-order traversal of a BST gives elements in:",
                "Random order", "Reverse order", "Sorted ascending order", "Level order", 2);
        addQuestion(q2, "What is the worst-case complexity of Merge Sort?",
                "O(n)", "O(n log n)", "O(n²)", "O(log n)", 1);
        addQuestion(q2, "A deque allows:",
                "Insertion at front only", "Deletion from rear only", "Insertion and deletion at both ends", "Only FIFO operations", 2);
        addQuestion(q2, "What is the height of a balanced BST with 15 nodes?",
                "3", "4", "5", "15", 0);
        addQuestion(q2, "Which sorting algorithm has O(n²) in the worst case but is efficient for small datasets?",
                "Merge Sort", "Insertion Sort", "Heap Sort", "Radix Sort", 1);

        System.out.println("  Created DSA category with 2 quizzes, 24 questions");
    }

    // =================================================================================
    // JAVA PROGRAMMING - 2 Quizzes, 12 Questions Each
    // =================================================================================
    private void createJavaCategory() {
        Category cat = categoryRepository.save(
                new Category("Java Programming", "OOP, JVM, Exception Handling, Collections & Multithreading", "bi-cup-hot", "#f59e0b"));

        // Quiz 1: OOP, JVM, Exception Handling
        Quiz q1 = quizRepository.save(new Quiz("OOP, JVM & Exception Handling", "Core Java concepts and runtime behavior", 15, cat));

        addQuestion(q1, "Which principle of OOP allows a class to inherit from another class?",
                "Encapsulation", "Polymorphism", "Inheritance", "Abstraction", 2);
        addQuestion(q1, "What does JVM stand for?",
                "Java Variable Machine", "Java Virtual Method", "Java Virtual Machine", "Java Verified Machine", 2);
        addQuestion(q1, "Which keyword is used to handle exceptions in Java?",
                "throw", "try", "catch", "All of the above", 3);
        addQuestion(q1, "What is the parent class of all classes in Java?",
                "Object", "Main", "Class", "Super", 0);
        addQuestion(q1, "Which of the following is a checked exception?",
                "NullPointerException", "ArrayIndexOutOfBoundsException", "IOException", "ArithmeticException", 2);
        addQuestion(q1, "What is method overloading?",
                "Same method name with different parameters in same class", "Same method in parent and child class", "Multiple inheritance", "Method with no parameters", 0);
        addQuestion(q1, "What is the purpose of the 'finally' block?",
                "Handles exceptions", "Executes always regardless of exception", "Throws exceptions", "Catches specific exceptions", 1);
        addQuestion(q1, "Which access modifier makes a member visible only within the same class?",
                "public", "protected", "default", "private", 3);
        addQuestion(q1, "What does JIT stand for in Java?",
                "Just In Time", "Java Inline Thread", "Java Interpreter Tool", "Just In Thread", 0);
        addQuestion(q1, "An abstract class in Java:",
                "Cannot have constructors", "Cannot have methods with body", "Can have both abstract and concrete methods", "Must have all abstract methods", 2);
        addQuestion(q1, "What is the output of: System.out.println(10 / 0)?",
                "0", "Infinity", "ArithmeticException", "Compilation Error", 2);
        addQuestion(q1, "Which keyword prevents a class from being inherited?",
                "static", "abstract", "final", "private", 2);

        // Quiz 2: Collections, Multithreading, I/O
        Quiz q2 = quizRepository.save(new Quiz("Collections & Multithreading", "Java Collections Framework and concurrent programming", 15, cat));

        addQuestion(q2, "Which collection class does NOT allow duplicate elements?",
                "ArrayList", "LinkedList", "HashSet", "Vector", 2);
        addQuestion(q2, "What is the difference between ArrayList and LinkedList?",
                "ArrayList uses array, LinkedList uses doubly linked list", "They are the same", "LinkedList is faster for random access", "ArrayList uses linked list", 0);
        addQuestion(q2, "Which interface does HashMap implement?",
                "List", "Set", "Map", "Queue", 2);
        addQuestion(q2, "How do you start a thread in Java?",
                "thread.run()", "thread.start()", "thread.execute()", "thread.begin()", 1);
        addQuestion(q2, "What is the purpose of the synchronized keyword?",
                "To make a method faster", "To prevent thread interference", "To create a new thread", "To stop a thread", 1);
        addQuestion(q2, "Which collection is thread-safe?",
                "ArrayList", "HashMap", "ConcurrentHashMap", "TreeSet", 2);
        addQuestion(q2, "What does the Comparable interface define?",
                "equals() method", "compareTo() method", "compare() method", "sort() method", 1);
        addQuestion(q2, "What is a deadlock?",
                "When a thread is stopped", "When two threads wait for each other indefinitely", "When memory runs out", "When CPU is overloaded", 1);
        addQuestion(q2, "Iterator vs ListIterator - what is correct?",
                "Both traverse in both directions", "ListIterator can traverse both directions", "Iterator can traverse both directions", "They are the same", 1);
        addQuestion(q2, "What is the default capacity of ArrayList?",
                "0", "5", "10", "16", 2);
        addQuestion(q2, "Which class provides buffered input reading in Java?",
                "FileReader", "BufferedReader", "Scanner only", "InputStream", 1);
        addQuestion(q2, "TreeMap stores entries sorted by:",
                "Insertion order", "Value", "Key", "Hash code", 2);

        System.out.println("  Created Java category with 2 quizzes, 24 questions");
    }

    // =================================================================================
    // PYTHON PROGRAMMING - 2 Quizzes, 12 Questions Each
    // =================================================================================
    private void createPythonCategory() {
        Category cat = categoryRepository.save(
                new Category("Python Programming", "Basics, Lists, Functions, OOP, Modules & File Handling", "bi-filetype-py", "#10b981"));

        // Quiz 1: Basics, Lists, Functions
        Quiz q1 = quizRepository.save(new Quiz("Python Basics & Functions", "Fundamentals of Python programming", 15, cat));

        addQuestion(q1, "Which keyword is used to define a function in Python?",
                "function", "func", "def", "define", 2);
        addQuestion(q1, "What is the output of: print(type([]))?",
                "<class 'tuple'>", "<class 'list'>", "<class 'dict'>", "<class 'set'>", 1);
        addQuestion(q1, "How do you create a list in Python?",
                "{1, 2, 3}", "(1, 2, 3)", "[1, 2, 3]", "<1, 2, 3>", 2);
        addQuestion(q1, "What does len() function do?",
                "Returns type", "Returns length", "Returns last element", "Returns sum", 1);
        addQuestion(q1, "Which of the following is immutable in Python?",
                "List", "Dictionary", "Set", "Tuple", 3);
        addQuestion(q1, "What is the output of: print(2 ** 3)?",
                "6", "8", "9", "5", 1);
        addQuestion(q1, "How do you add an element to a list?",
                "list.add()", "list.append()", "list.insert_end()", "list.push()", 1);
        addQuestion(q1, "What is a lambda function?",
                "A named function", "An anonymous inline function", "A class method", "A module", 1);
        addQuestion(q1, "What does the 'pass' keyword do?",
                "Exits a loop", "Raises an exception", "Does nothing (placeholder)", "Returns None", 2);
        addQuestion(q1, "What is the output of: print('Hello'[1])?",
                "H", "e", "He", "Error", 1);
        addQuestion(q1, "Which method removes the last element from a list?",
                "remove()", "pop()", "delete()", "discard()", 1);
        addQuestion(q1, "What is list slicing syntax for getting elements 2 to 5?",
                "list[2:5]", "list[2,5]", "list(2:5)", "list{2:5}", 0);

        // Quiz 2: OOP, Modules, File Handling
        Quiz q2 = quizRepository.save(new Quiz("Python OOP & Advanced", "Object-oriented programming and advanced Python", 15, cat));

        addQuestion(q2, "What keyword is used to create a class in Python?",
                "struct", "class", "object", "define", 1);
        addQuestion(q2, "What is __init__ in Python?",
                "Destructor", "Constructor", "Iterator", "Generator", 1);
        addQuestion(q2, "What is 'self' in Python class methods?",
                "A keyword for static methods", "A reference to the current instance", "A global variable", "A class name", 1);
        addQuestion(q2, "How do you import a module in Python?",
                "include module", "require module", "import module", "#include module", 2);
        addQuestion(q2, "What does 'pip' stand for?",
                "Python Install Packages", "Pip Installs Packages", "Python Integrated Platform", "Package Index Python", 1);
        addQuestion(q2, "Which function opens a file in Python?",
                "file()", "open()", "read()", "os.open()", 1);
        addQuestion(q2, "What is inheritance in Python?",
                "Creating multiple objects", "A class deriving from another class", "Deleting a class", "Function overloading", 1);
        addQuestion(q2, "What does the 'with' keyword do when opening files?",
                "Opens file in binary mode", "Ensures file is properly closed after operations", "Opens multiple files", "Creates a backup", 1);
        addQuestion(q2, "What is a decorator in Python?",
                "A class attribute", "A function that modifies another function", "A type of loop", "A module", 1);
        addQuestion(q2, "Which built-in function converts a string to an integer?",
                "str()", "float()", "int()", "chr()", 2);
        addQuestion(q2, "What is a Python generator?",
                "A class factory", "A function that uses yield to return values lazily", "A type of list", "A file reader", 1);
        addQuestion(q2, "What is the output of: print(bool(''))?",
                "True", "False", "None", "Error", 1);

        System.out.println("  Created Python category with 2 quizzes, 24 questions");
    }

    // =================================================================================
    // MACHINE LEARNING - 2 Quizzes, 12 Questions Each
    // =================================================================================
    private void createMLCategory() {
        Category cat = categoryRepository.save(
                new Category("Machine Learning", "Supervised & Unsupervised Learning, Algorithms & Terminology", "bi-robot", "#ec4899"));

        // Quiz 1: Supervised vs Unsupervised, Regression
        Quiz q1 = quizRepository.save(new Quiz("ML Fundamentals & Regression", "Core concepts of machine learning and regression techniques", 15, cat));

        addQuestion(q1, "What is Machine Learning?",
                "Programming explicit rules", "Systems learning from data without being explicitly programmed", "Database management", "Web development framework", 1);
        addQuestion(q1, "What is supervised learning?",
                "Learning without labels", "Learning with labeled training data", "Learning by clustering", "Learning by reinforcement", 1);
        addQuestion(q1, "Which of the following is a supervised learning algorithm?",
                "K-Means", "DBSCAN", "Linear Regression", "PCA", 2);
        addQuestion(q1, "What is the target variable in regression?",
                "Categorical", "Continuous", "Binary only", "Text", 1);
        addQuestion(q1, "What does MSE stand for?",
                "Maximum Squared Error", "Mean Squared Error", "Minimum Standard Error", "Mean Standard Estimation", 1);
        addQuestion(q1, "In Linear Regression, the relationship between input and output is:",
                "Non-linear", "Linear", "Exponential", "Logarithmic only", 1);
        addQuestion(q1, "What is overfitting?",
                "Model performs well on both training and test data", "Model performs well on training but poorly on test data", "Model performs poorly on all data", "Model has too few features", 1);
        addQuestion(q1, "What is the purpose of a training set?",
                "To evaluate model performance", "To train the model parameters", "To deploy the model", "To visualize data", 1);
        addQuestion(q1, "What is a feature in Machine Learning?",
                "The output variable", "An input variable used for prediction", "The model name", "The dataset size", 1);
        addQuestion(q1, "What does R² (R-squared) measure?",
                "Model complexity", "Proportion of variance explained by the model", "Number of features", "Training time", 1);
        addQuestion(q1, "What is cross-validation?",
                "Training on entire dataset", "Splitting data into multiple folds for training and testing", "Testing on training data", "Using only one sample", 1);
        addQuestion(q1, "Which of these helps prevent overfitting?",
                "Adding more features", "Reducing training data", "Regularization", "Removing validation set", 2);

        // Quiz 2: Classification, Clustering, Terminology
        Quiz q2 = quizRepository.save(new Quiz("Classification & Clustering", "Classification algorithms, unsupervised learning, and ML terminology", 15, cat));

        addQuestion(q2, "What is classification in ML?",
                "Predicting continuous values", "Predicting categorical labels", "Grouping similar data", "Reducing dimensions", 1);
        addQuestion(q2, "What is unsupervised learning?",
                "Learning with labeled data", "Learning from unlabeled data to find patterns", "Learning with rewards", "Learning with test data", 1);
        addQuestion(q2, "Which algorithm is used for clustering?",
                "Linear Regression", "Logistic Regression", "K-Means", "Decision Tree", 2);
        addQuestion(q2, "What does the 'K' in K-Nearest Neighbors represent?",
                "Number of features", "Number of nearest neighbors to consider", "Number of clusters", "Learning rate", 1);
        addQuestion(q2, "What is a confusion matrix?",
                "A matrix showing model architecture", "A table showing predicted vs actual classifications", "A data preprocessing tool", "A feature selection method", 1);
        addQuestion(q2, "What is precision in classification?",
                "True Positives / (True Positives + False Negatives)", "True Positives / (True Positives + False Positives)", "Total correct / Total predictions", "False Positives / Total", 1);
        addQuestion(q2, "What type of algorithm is Decision Tree?",
                "Unsupervised only", "Both supervised classification and regression", "Clustering only", "Dimensionality reduction", 1);
        addQuestion(q2, "What is the purpose of PCA (Principal Component Analysis)?",
                "Classification", "Dimensionality reduction", "Clustering", "Regression", 1);
        addQuestion(q2, "What is a neural network inspired by?",
                "Computer circuits", "Biological neurons", "Mathematical proofs", "Database systems", 1);
        addQuestion(q2, "What does the bias-variance tradeoff mean?",
                "Choosing between CPU and GPU", "Balancing model simplicity (bias) with flexibility (variance)", "Trading accuracy for speed", "Balancing training and test size", 1);
        addQuestion(q2, "What is recall (sensitivity) in ML?",
                "True Positives / (True Positives + False Positives)", "True Positives / (True Positives + False Negatives)", "Accuracy of the model", "Precision squared", 1);
        addQuestion(q2, "Which is NOT an evaluation metric for classification?",
                "Accuracy", "F1-Score", "Mean Squared Error", "Precision", 2);

        System.out.println("  Created ML category with 2 quizzes, 24 questions");
    }
}
