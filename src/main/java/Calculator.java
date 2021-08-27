import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame {
    static JPanel outPanel = new JPanel();
    static JLabel outText = new JLabel();
    int skinNum = 0;

    public Calculator() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 5, 5));
        JLabel memLabel = new JLabel("");
        JButton arrBut[] = {new JButton("MR"), new JButton("MC"), new JButton("M+"), new JButton("C"),
                new JButton("<-"), new JButton("+/-"), new JButton("Color"), new JButton("7"), new JButton("8"),
                new JButton("9"), new JButton("*"), new JButton("4"), new JButton("5"), new JButton("6"),
                new JButton("/"), new JButton("1"), new JButton("2"), new JButton("3"), new JButton("-"),
                new JButton("0"), new JButton(","), new JButton("="), new JButton("+")};

        setTitle("Калькулятор");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(200, 200, 600, 700);
        setResizable(false);
        setVisible(true);

        Operand op = new Operand();
        Memory mem = new Memory();

        Font font = new Font("Verdana", Font.BOLD, 40); //шрифт экрана
        Font fontMem = new Font("Verdana", Font.BOLD, 30); //Шрифт экранчика памяти
        Font fontBut = new Font("Verdana", Font.BOLD, 30); //Шрифт кнопок

        Border myBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        outText.setFont(font);
        outText.setVerticalAlignment(JLabel.CENTER);
        outText.setHorizontalAlignment(JLabel.RIGHT);
        outPanel.setBorder(myBorder);
        outPanel.setBackground(Color.LIGHT_GRAY);
        outText.setPreferredSize(new Dimension(600, 150));

        memLabel.setFont(fontMem);
        memLabel.setVerticalAlignment(JLabel.CENTER);
        memLabel.setHorizontalAlignment(JLabel.CENTER);
        memLabel.setForeground(Color.RED);
        buttonPanel.add(memLabel);
        add(buttonPanel, BorderLayout.CENTER);
//        //Масив цветов
        Color skin[][] = new Color[3][4];
        skin[0] = new Color[]{Color.BLUE, Color.BLACK, Color.WHITE, Color.LIGHT_GRAY};
        skin[1] = new Color[]{Color.RED, Color.WHITE, Color.BLACK, Color.DARK_GRAY};
        skin[2] = new Color[]{Color.BLACK, Color.DARK_GRAY, Color.blue, Color.orange};
//Устанавливаем начальные цвета
        outText.setForeground(skin[skinNum][0]);
        buttonPanel.setBackground(skin[skinNum][2]);

//Добавляем кнопки, устанавливаем их цвета
        for (int i = 0; i < arrBut.length; i++) {
            arrBut[i].setFont(fontBut);
            arrBut[i].setBackground(skin[skinNum][3]);
            arrBut[i].setForeground(skin[skinNum][1]);
            buttonPanel.add(arrBut[i]);
        }
        outPanel.add(outText);
        outPanel.setLayout(new GridLayout(1, 1, 5, 5));
        add(outPanel, BorderLayout.NORTH);

        //Цифровые кнопки
        //0
        arrBut[19].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(0);
                setOutText(op.getOper());
            }
        });

        //1
        arrBut[15].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(1);
                setOutText(op.getOper());
            }
        });

        //2
        arrBut[16].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(2);
                setOutText(op.getOper());
            }
        });

        //3
        arrBut[17].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(3);
                setOutText(op.getOper());
            }
        });

        //4
        arrBut[11].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(4);
                setOutText(op.getOper());
            }
        });

        //5
        arrBut[12].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(5);
                setOutText(op.getOper());
            }
        });

        //6
        arrBut[13].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(6);
                setOutText(op.getOper());
            }
        });

        //7
        arrBut[7].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(7);
                setOutText(op.getOper());
            }
        });

        //8
        arrBut[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(8);
                setOutText(op.getOper());
            }
        });

        //9
        arrBut[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.addDigit(9);
                setOutText(op.getOper());
            }
        });


//Кнопки операций, применяем предыдущую операцию, выводим результат, начинаем следующую операцию
        //Знак плюс
        arrBut[22].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                outText.setText("" + op.getResultInt());
                op.resetoper();
                op.setSign('+');
            }
        });
        //знак минус
        arrBut[18].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                outText.setText("" + op.getResultInt());
                op.resetoper();
                op.setSign('-');
            }
        });
        //Деление
        arrBut[14].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                outText.setText("" + op.getResultInt());
                op.resetoper();
                op.setSign('/');
            }
        });
        //Умножение
        arrBut[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                outText.setText("" + op.getResultInt());
                op.resetoper();
                op.setSign('*');
            }
        });


//Кнопка =
        arrBut[21].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                outText.setText("" + op.getResultInt());
                op.resetoper();
            }
        });
//Сброс кнопка С
        arrBut[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                outText.setText("");
                op.fullReset();
            }
        });
//BackSpace
        arrBut[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.minusDigit();
                outText.setText("" + op.getOper());
            }
        });

//изменить знак операнда
        arrBut[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.changeSign();
                outText.setText("" + op.getOper());
            }
        });
//изменить Скин
        arrBut[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                skinNum = (skinNum + 1) % skin.length;
                outText.setForeground(skin[skinNum][0]);
                buttonPanel.setBackground(skin[skinNum][2]);
                outPanel.setBackground(skin[skinNum][3]);
//Меняем цвет кнопок
                for (int i = 0; i < arrBut.length; i++) {
                    arrBut[i].setBackground(skin[skinNum][3]);
                    arrBut[i].setForeground(skin[skinNum][1]);
                }
            }
        });

//работа с памятью
        //М+, добавить результат в память
        arrBut[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.apply();
                mem.addMemmory(op.getResultInt());
                memLabel.setText("M");
                outText.setText("");
                op.fullReset();
            }
        });
        //очистить память МС
        arrBut[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mem.memmoryReset();
                memLabel.setText("");
            }
        });
        //Достать данные из памяти
        arrBut[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.setOperInt(mem.getMemmory());
                outText.setText("" + op.getOper());
            }
        });


        arrBut[20].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                op.resetoper();
                outText.setText("Это не работает =(");
            }
        });


    }

    //Вывод текста на экран калькулятора
    public void setOutText(long text) {
        outText.setText("" + text);
    }

    public void setOutText(float text) {
        outText.setText("" + text);
    }

}
