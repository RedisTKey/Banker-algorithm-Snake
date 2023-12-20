import java.util.Arrays;
import java.util.Random;

public class BankersAlgorithm {

    private int processes; // 进程数
    private int resources; // 资源数
    private int[][] max; // 最大需求矩阵
    private int[][] allocated; // 已分配矩阵
    private int[] available; // 可用资源向量

    public BankersAlgorithm(int processes, int resources) {
        this.processes = processes;
        this.resources = resources;
        this.max = new int[processes][resources];
        this.allocated = new int[processes][resources];
        this.available = new int[resources];

        // 初始化 max 矩阵和 allocated 矩阵
        initializeMatrices();

        // 初始化 available 向量，确保生成的向量使系统处于安全状态
        initializeAvailable();
    }

    private void initializeMatrices() {
        Random rand = new Random();

        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                // 随机生成最大需求矩阵
                max[i][j] = rand.nextInt(5) + 1;
                // 随机生成已分配矩阵，不超过最大需求
                allocated[i][j] = rand.nextInt(max[i][j] );
            }
        }
    }

    private void initializeAvailable() {
        Random rand = new Random();

        while (true) {
            for (int j = 0; j < resources; j++) {
                // 随机生成可用资源向量，保证至少有一个资源是足够的
                available[j] = rand.nextInt(11);
            }

            if (isSafe()) {
                break;
            }
        }
    }

    private boolean isSafe() {
        int[] work = Arrays.copyOf(available, available.length);
        boolean[] finish = new boolean[processes];

        for (int i = 0; i < processes; i++) {
            if (!finish[i] && canFinish(i, work)) {
                finish[i] = true;
                for (int j = 0; j < resources; j++) {
                    work[j] += allocated[i][j];
                }
                i = -1; // Restart the loop
            }
        }

        // Check if all processes are finished
        for (boolean isFinished : finish) {
            if (!isFinished) {
                return false; // Unsafe state
            }
        }

        return true; // Safe state
    }

    private boolean canFinish(int process, int[] work) {
        for (int j = 0; j < resources; j++) {
            if (max[process][j] - allocated[process][j] > work[j]) {
                return false;
            }
        }
        return true;
    }

    public void runBankersAlgorithm() {
        System.out.println("Initial Max Matrix:");
        printMatrix(max);
        System.out.println("Initial Allocated Matrix:");
        printMatrix(allocated);
        System.out.println("Initial Available Vector: " + Arrays.toString(available));

        // 执行银行家算法
        int[] safeSequence = findSafeSequence();
        if (safeSequence != null) {
            System.out.println("System is in a safe state. Safe sequence: " + Arrays.toString(safeSequence));
        } else {
            System.out.println("Error: Unable to find a safe state.");
        }
    }

    private int[] findSafeSequence() {
        int[] work = Arrays.copyOf(available, available.length);
        boolean[] finish = new boolean[processes];
        int[] safeSequence = new int[processes];
        int index = 0;

        while (index < processes) {
            boolean found = false;
            for (int i = 0; i < processes; i++) {
                if (!finish[i] && canFinish(i, work)) {
                    finish[i] = true;
                    safeSequence[index++] = i;
                    for (int j = 0; j < resources; j++) {
                        work[j] += allocated[i][j];
                    }
                    found = true;
                }
            }

            if (!found) {
                return null; // No safe sequence found
            }
        }

        return safeSequence;
    }

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    /**================================== 用户可以调用的方法 ==================================*/
    public int[][] getMax(){return this.max;}
    public int[][] getAllocated(){return this.allocated;}
    public int[][] getNeed(){
        int[][] need = new int[max.length][max[0].length];
        for (int i = 0; i < max.length; i++) {
            for (int j = 0; j < max[i].length; j++) {
                need[i][j] = max[i][j]-allocated[i][j];
            }
        }
        return need;
    }
    public int[] getAvailable(){return this.available;}

    public int getProcesses(){return this.processes;}
    public int getResources(){return this.resources;}

    public static void main(String[] args) {
        int processes = 5; // 进程数
        int resources = 3; // 资源数

        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm(processes, resources);
        bankersAlgorithm.runBankersAlgorithm();
        bankersAlgorithm.printMatrix(bankersAlgorithm.getNeed());
    }
}
