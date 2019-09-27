import java.util.ArrayList;
import java.util.Scanner;

public class Reversi {

  // try it 

  public static char[][] board = new char[26][26];

  public static void printBoard(char[][] board, int n) {
    board[0][0] = ' ';
    for (int i = 1; i <= n; i++) {
      board[0][i] = (char) ('a' + i - 1);
      board[i][0] = (char) ('a' + i - 1);
    }

    for (int i = 0; i <= n; i++) {
      for (int j = 0; j <= n; j++) {
        if (j == 0) {
          System.out.print(board[i][j]);
          System.out.print(' ');
        } else {
          System.out.print(board[i][j]);
        }
      }
      System.out.println("");
    }
  }

  public static boolean checkLegalInDirection(char board[][], int n, int row, int col, char colour,
      int deltaRow, int deltaCol) {

    int unitMoveRow = deltaRow;
    int unitMoveCol = deltaCol;
    if (positionInBounds(n, row + deltaRow, col + deltaCol) && board[row][col] == 'U') {
      while (board[row + deltaRow][col + deltaCol] != 'U'
          && board[row + deltaRow][col + deltaCol] != colour) {
        deltaRow += unitMoveRow;
        deltaCol += unitMoveCol;
        if (positionInBounds(n, row + deltaRow, col + deltaCol)) {
          if (board[row + deltaRow][col + deltaCol] == colour) {
            return true;
          }
        } else {
          break;
        }
      }
    }
    return false;
  }

  public static boolean checkAllDirection(char board[][], int n, int row, int col, char colour) {

    return checkLegalInDirection(board, n, row, col, colour, 1, 1)
        || checkLegalInDirection(board, n, row, col, colour, 1, 0)
        || checkLegalInDirection(board, n, row, col, colour, 1, -1)
        || checkLegalInDirection(board, n, row, col, colour, 0, 1)
        || checkLegalInDirection(board, n, row, col, colour, 0, -1)
        || checkLegalInDirection(board, n, row, col, colour, -1, 1)
        || checkLegalInDirection(board, n, row, col, colour, -1, -1)
        || checkLegalInDirection(board, n, row, col, colour, -1, 0);

  }

  public static boolean positionInBounds(int n, int row, int col) {
    if (row >= 1 && row <= n && col >= 1 && col <= n) {
      return true;
    } else {
      return false;
    }
  }

  public static void boardConfiguration(char[][] board) {}


  public static void printLegalMoves(char[][] board, int n, char color) {
    System.out.println("Avaiable moves for " + color);
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        if (checkAllDirection(board, n, i, j, color)) {
          System.out.println((char) ('a' + i - 1) + "" + (char) ('a' + j - 1));
        }
      }
    }
  }

  public static int optimalMove(char[][] board, int n, char color) {

    ArrayList<Integer> possibleMoves = new ArrayList<>();

    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        if (checkAllDirection(board, n, i, j, color)) {
          possibleMoves.add(i * 10 + j);
        }
      }
    }

    int optimalCombination = checkCredits(n, possibleMoves).get(0);
    return optimalCombination;

  }

  public static ArrayList<Integer> checkCredits(int n, ArrayList<Integer> a) {

    ArrayList<Integer> creditsBoard = new ArrayList<Integer>();
    for (int i = 0; i < a.size(); i++) {
      if (creditsBoard.size() == 0) {
        creditsBoard.add(credits(n, a.get(i) / 10, a.get(i) % 10));
      } else {
        int j = 0;
        int credits = credits(n, a.get(i) / 10, a.get(i) % 10);
        while (creditsBoard.get(j) > credits) {
          j++;
        }
        creditsBoard.add(j + 1, credits);
      }
    }
    return creditsBoard;

  }

  public static int credits(int n, int row, int col) {
    return row + col;
  }

  public static void changeOneDirection(char[][] board, int n, int row, int col, char colour,
      int deltaRow, int deltaCol) {

    int unitMoveRow = deltaRow;
    int unitMoveCol = deltaCol;
    int count = 0;
    while (board[row + deltaRow][col + deltaCol] != colour) {
      deltaRow = deltaRow + unitMoveRow;
      deltaCol = deltaCol + unitMoveCol;
      count++;
      if (!positionInBounds(n, row + deltaRow, col + deltaCol)) {
        count = 0;
        break;
      }
    }
    if (count != 0) {
      for (int i = 1; i <= count; i++) {
        board[row + unitMoveRow * i][col + unitMoveCol * i] = colour;
      }

    }


    System.out.println("Count: " + count);
  }

  public static void changeAllDirection(char[][] board, int n, int row, int col, char colour) {

    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i != 0 || j != 0) {
          System.out.print("i: " + i);
          System.out.print("    j: " + j + "\t");
          System.out.print(checkLegalInDirection(board, n, row, col, colour, i, j));
          System.out.println(" ");
          if (checkLegalInDirection(board, n, row, col, colour, i, j)) {

            changeOneDirection(board, n, row, col, colour, i, j);

          }
        }
      }
    }
    board[row][col] = colour;
  }

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int n = 8;
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        board[i][j] = 'U';
      }
    }

    board[(n / 2)][(n / 2)] = 'W';
    board[n / 2 + 1][n / 2 + 1] = 'W';
    board[(n / 2)][n / 2 + 1] = 'B';
    board[n / 2 + 1][(n / 2)] = 'B';

    printBoard(board, n);

    printLegalMoves(board, n, 'W');
    printLegalMoves(board, n, 'B');

    System.out.println("Enter a move:");
    String commands = scan.nextLine().trim();
    char colour = commands.charAt(0);
    int row = (int) (commands.toLowerCase().charAt(1)) - 'a' + 1;
    int col = (int) (commands.toLowerCase().charAt(2)) - 'a' + 1;

    while (true) {
      if (checkAllDirection(board, n, row, col, colour)) {
        System.out.println("Valid Move");
        changeAllDirection(board, n, row, col, colour);
      } else {
        System.out.println("Invalid Move");

      }

      printBoard(board, n);
      printLegalMoves(board, n, 'W');
      printLegalMoves(board, n, 'B');

      System.out.println("Enter a move:");
      commands = scan.nextLine().trim();
      colour = commands.charAt(0);
      row = (int) (commands.toLowerCase().charAt(1)) - 'a' + 1;
      col = (int) (commands.toLowerCase().charAt(2)) - 'a' + 1;

    }
  }
}


