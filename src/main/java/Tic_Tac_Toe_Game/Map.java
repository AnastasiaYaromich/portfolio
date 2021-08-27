package Tic_Tac_Toe_Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Map extends JPanel {

    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;

    private static final int DOT_EMPTY = 0;
    private static final int DOT_X = 1;
    private static final int DOT_O = 2;

    private static final int STANDOFF = 0;
    private static final int X_WIN = 1;
    private static final int O_WIN = 2;
    private int stateGameOver;

    private static final String messageStandoff = "Ничья!";
    private static final String messageX_WIN = "Победили крестики!";
    private static final String messageO_WIN = "Победили нолики!";

    private static boolean move_X = true;
    private static boolean move_O = false;

    private final Random random = new Random();
    private final Font font = new Font("Times New Roman", Font.BOLD, 36);
    private static int[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    static int winLen;
    static int mode;
    int cellHeight;
    int cellWidth;
    private int cellX;
    private int cellY;
    boolean isInitializated = false;
    private boolean GameOver;
    private BufferedImage image;

    public Map() throws IOException {
        image = ImageIO.read(new File("src/castle.jpg"));
        // setBackground(Color.ORANGE);
        setVisible(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
    }

    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {
        System.out.println(mode + " " + fieldSizeX + " " + fieldSizeY + " " + winLength);
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLength;
        field = new int[fieldSizeY][fieldSizeX];
        this.mode = mode;
        isInitializated = true;
        GameOver = false;
        repaint();
    }

    void update(MouseEvent e) {
        cellX = e.getX() / this.cellWidth;
        cellY = e.getY() / this.cellHeight;
        System.out.println("x: " + cellX + " y: " + cellY);
        repaint();
        if (GameOver) return;
        if (mode == MODE_H_V_A) {
            modeHumanVsAi();
        } else if (mode == MODE_H_V_H) {
            modeHumanVsHuman();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        BasicStroke line = new BasicStroke(3);
        g2D.setStroke(line);
        g.drawImage(image, 0, 0, this);
        render(g);
    }

    void render(Graphics g) {
        if (!isInitializated) return;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelHeight, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelWidth);
        }

        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] != DOT_EMPTY) {
                    if (field[i][j] == DOT_X) {
                        g.setColor(Color.white);
                        g.drawLine((j * cellHeight) + 10, (i * cellWidth) + 10, (j + 1) * cellHeight - 10,
                                (i + 1) * cellWidth - 10);

                        g.drawLine((j * cellHeight) + 10, (i + 1) * cellWidth - 10, (j + 1) * cellHeight - 10,
                                (i * cellWidth) + 10);
                    }
                    if (field[i][j] == DOT_O) {
                        g.setColor(Color.pink);
                        g.drawOval((j * cellHeight) + 10, (i * cellWidth) + 10, cellHeight - 20,
                                cellWidth - 20);
                    }
                }
            }
        }
        if (GameOver) {
            showMessage(g);
        }
    }

    private void showMessage(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.WHITE);
        g.setFont(font);

        switch (stateGameOver) {
            case STANDOFF:
                g.drawString(messageStandoff, 180, getHeight() / 2);
                break;

            case X_WIN:
                g.drawString(messageX_WIN, 45, getHeight() / 2);
                break;

            case O_WIN:
                g.drawString(messageO_WIN, 50, getHeight() / 2);
                break;
        }
    }

    private void modeHumanVsHuman() {
        if (!isCellValid(cellY, cellX)) {
            return;
        } else if (move_X) {
            field[cellY][cellX] = DOT_X;
            move_X = !move_X;
            move_O = !move_O;
            repaint();
        }
        if (checkWin(DOT_X)) {
            stateGameOver = X_WIN;
            GameOver = true;
            return;
        }
        if (isMapsFull()) {
            stateGameOver = STANDOFF;
            GameOver = true;
            return;
        }
        repaint();
        if (!isCellValid(cellY, cellX)) {
            return;
        } else if (move_O) {
            field[cellY][cellX] = DOT_O;
            move_O = !move_O;
            move_X = !move_X;
            repaint();
        }

        if (checkWin(DOT_O)) {
            stateGameOver = O_WIN;
            GameOver = true;
            return;
        }
        if (isMapsFull()) {
            stateGameOver = STANDOFF;
            GameOver = true;
            return;
        }
        repaint();
    }


    private void modeHumanVsAi() {
        playerStep();
        repaint();
        if (checkWin(DOT_X)) {
            stateGameOver = X_WIN;
            GameOver = true;
            return;
        }
        if (isMapsFull()) {
            stateGameOver = STANDOFF;
            GameOver = true;
            return;
        }
        artinrelStep();
        repaint();
        if (checkWin(DOT_O)) {
            stateGameOver = O_WIN;
            GameOver = true;
            return;
        }
        if (isMapsFull()) {
            stateGameOver = STANDOFF;
            GameOver = true;
            return;
        }
    }


    public void playerStep() {
        if (!isCellValid(cellY, cellX)) {
            return;
        } else {
            field[cellY][cellX] = DOT_X;
        }
    }

    public static boolean isCellValid(int y, int x) {
        if (x < 0 || y < 0 || x > fieldSizeX - 1 || y > fieldSizeY - 1) {
            return false;
        }
        return field[y][x] == DOT_EMPTY;
    }

    public static boolean checkWin(int dot) {
        for (int vert = 0; vert < fieldSizeY; vert++) {
            for (int hor = 0; hor < fieldSizeX; hor++) {
                if (hor + winLen <= fieldSizeX) {
                    if (checkLineOnHorizont(vert, hor, dot) >= winLen)
                        return true;

                    if (vert - winLen > -2) {
                        if (checkDiagUp(vert, hor, dot) >= winLen)
                            return true;
                    }


                    if (vert + winLen <= fieldSizeY) {
                        if (checkDiagDown(vert, hor, dot) >= winLen)
                            return true;
                    }
                }


                if (vert + winLen <= fieldSizeY) {
                    if (checkLineOnVertical(vert, hor, dot) >= winLen)
                        return true;
                }
            }
        }
        return false;
    }

    public static int checkDiagUp(int vert, int hor, int dot) {
        int count = 0;
        for (int i = 0, j = 0; j < winLen; i--, j++) {
            if (field[vert + i][hor + j] == dot) count++;
        }
        return count;
    }

    public static int checkDiagDown(int vert, int hor, int dot) {
        int count = 0;
        for (int i = 0; i < winLen; i++) {
            if ((field[vert + i][hor + i] == dot)) count++;
        }
        return count;
    }

    public static int checkLineOnHorizont(int vert, int hor, int dot) {
        int count = 0;
        for (int j = hor; j < winLen + hor; j++) {
            if (field[vert][j] == dot) count++;
        }
        return count;
    }

    public static int checkLineOnVertical(int vert, int hor, int dot) {
        int count = 0;
        for (int i = vert; i < winLen + vert; i++) {
            if (field[i][hor] == dot) count++;
        }
        return count;
    }

    public static boolean isMapsFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void artinrelStep() {
        int x;
        int y;

        for (int vert = 0; vert < fieldSizeY; vert++) {
            for (int hor = 0; hor < fieldSizeX; hor++) {
                if (hor + winLen <= fieldSizeX) {
                    if (checkLineOnHorizont(vert, hor, DOT_X) == winLen - 1) {
                        if (MoveAiLineHorizont(vert, hor, DOT_O)) return;
                    }

                    if (vert - winLen > -2) {
                        if (checkDiagUp(vert, hor, DOT_X) == winLen - 1) {
                            if (MoveAiDiaUp(vert, hor, DOT_O)) return;
                        }
                    }

                    if (vert + winLen <= fieldSizeY) {
                        if (checkDiagDown(vert, hor, DOT_X) == winLen - 1) {
                            if (MoveAiDiaDown(vert, hor, DOT_O)) return;
                        }
                    }
                }

                if (vert + winLen <= fieldSizeY) {
                    if (checkLineOnVertical(vert, hor, DOT_X) == winLen - 1) {
                        if (MoveAiLineVertical(vert, hor, DOT_O)) return;
                    }
                }
            }
        }

        for (int vert = 0; vert < fieldSizeY; vert++) {
            for (int hor = 0; hor < fieldSizeX; hor++) {
                if (hor + winLen <= fieldSizeX) {
                    if (checkLineOnHorizont(vert, hor, DOT_O) == winLen - 1) {
                        if (MoveAiLineHorizont(vert, hor, DOT_O)) return;
                    }

                    if (vert - winLen > -2) {
                        if (checkDiagUp(vert, hor, DOT_O) == winLen - 1) {
                            if (MoveAiDiaUp(vert, hor, DOT_O)) return;
                        }
                    }

                    if (vert + winLen <= fieldSizeY) {
                        if (checkDiagDown(vert, hor, DOT_O) == winLen - 1) {
                            if (MoveAiDiaDown(vert, hor, DOT_O)) return;
                        }
                    }
                }

                if (vert + winLen <= fieldSizeY) {
                    if (checkLineOnVertical(vert, hor, DOT_O) == winLen - 1) {
                        if (MoveAiLineVertical(vert, hor, DOT_O)) return;
                    }
                }
            }
        }

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellValid(y, x));
        field[y][x] = DOT_O;
    }

    public static boolean MoveAiLineHorizont(int vert, int hor, int dot) {
        for (int j = hor; j < winLen; j++) {
            if (field[vert][j] == DOT_EMPTY) {
                field[vert][j] = dot;
                return true;
            }
        }
        return false;
    }

    public static boolean MoveAiLineVertical(int vert, int hor, int dot) {
        for (int i = vert; i < winLen; i++) {
            if (field[i][hor] == DOT_EMPTY) {
                field[i][hor] = dot;
                return true;
            }
        }
        return false;
    }

    public static boolean MoveAiDiaUp(int vert, int hor, int dot) {
        for (int i = 0, j = 0; j < winLen; i--, j++) {
            if (field[vert + i][hor + j] == DOT_EMPTY) {
                field[vert + i][hor + j] = dot;
                return true;
            }
        }
        return false;
    }

    public static boolean MoveAiDiaDown(int vert, int hor, int dot) {
        for (int i = 0; i < winLen; i++) {
            if (field[vert + i][hor + i] == DOT_EMPTY) {
                field[vert + i][hor + i] = dot;
                return true;
            }
        }
        return false;
    }
}


