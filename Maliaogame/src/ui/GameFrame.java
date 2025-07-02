package ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mario.Mario;
import mario.Mario2;
import role.*;
import util.DatabaseUtil;
import util.GameRecordDAO;
import util.Map;
import util.MusicUtil;

public class GameFrame extends JFrame {
    // ��������:������Ҫ������������
    public Mario mario;
    public Mario2 mario2;
    // �ֱ���:ˮ�ܣ���Һ�ש��
    public Enemy pipe, coin, brick, wave, mogu, castle;

    // �����Ѫ��
    public Blood marioblood1, marioblood2, marioblood3;
    public Blood marioblood21, marioblood22, marioblood23;
    // ����ͼƬ
    public BackgroundImage bg;
    // ��ײ���
    public boolean hitflag1 = false, hitflag2 = false;
    // ����һ����������װ�ϰ������
    public ArrayList<Enemy> eneryList = new ArrayList<Enemy>();
    // ����һ����������װ�ӵ�
    public ArrayList<Boom> boomList = new ArrayList<Boom>();
    public ArrayList<Boom> boomList2 = new ArrayList<Boom>();
    // �ӵ����ٶ�
    public int bspeed = 0;
    public int mapnum = 1;
    public boolean changeflag = false; // �����Ѫ���ı���
    public boolean changeflag2 = false;
    public boolean endflag = false; // ��Ϸ�������
    public int score1 = 0; // ���1�÷�
    public int score2 = 0; // ���2�÷�
    // ��ͼ����
    public int[][] map = null;
    private int userid;
    private String username;
    private boolean isPaused = false; // ��Ϸ��ͣ��־

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public GameFrame(int num, int userid, String username) throws Exception {
        this.mapnum = num;
        this.userid = userid;
        this.username = username;

        {
            Map mp = new Map();
            map = mp.readMap(mapnum);
        }

        // ��Ϸ�淨��ʾ����
        JOptionPane.showMessageDialog(
                null,
                "�淨˵��:\n" +
                        "1P��\n - ��/�� ���ƶ�\n - �� ����Ծ\n - �ո�� �����ӵ�\n" +
                        "2P��\n - A/D ���ƶ�\n - W ����Ծ\n - S �����ӵ�\n" +
                        "�� P ����ͣ��Ϸ\n�� Q ���˳���Ϸ",
                "��Ϸ�淨��ʾ",
                JOptionPane.INFORMATION_MESSAGE
        );

        this.setSize(800, 450);
        this.setTitle("��������");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        mario = new Mario(this);
        mario2 = new Mario2(this);
        this.score1 = mario.score;
        this.score2 = mario2.score2;
        bg = new BackgroundImage();

        // ��ͼ���� �������ֵ��߶���
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                switch (map[i][j]) {
                    case 0:
                        break;
                    case 1:
                        brick = new Brick(j * 30, i * 30, 30, 30, new ImageIcon("image/brick.png").getImage());
                        eneryList.add(brick);
                        break;
                    case 2:
                        coin = new Coin(j * 30, i * 30, 30, 30, new ImageIcon("image/coin_brick.png").getImage());
                        eneryList.add(coin);
                        break;
                    case 3:
                        pipe = new Pipe(j * 30, i * 30, 60, 120, new ImageIcon("image/pipe.png").getImage());
                        eneryList.add(pipe);
                        break;
                    case 4:
                        wave = new Wave(j * 30, i * 30, 30, 30, new ImageIcon("image/wave.png").getImage());
                        eneryList.add(wave);
                        break;
                    case 5:
                        mogu = new Mogu(j * 30, i * 30, 30, 30, new ImageIcon("image/mogu.png").getImage());
                        eneryList.add(mogu);
                        break;
                    case 6:
                        castle = new Castle(j * 30, i * 30, 90, 90, new ImageIcon("image/tower.png").getImage());
                        eneryList.add(castle);
                        break;
                    case 7:
                        Turtle turtle = new Turtle(j * 30, i * 30, 30, 30, new ImageIcon("image/Ltortoise2.png").getImage());
                        eneryList.add(turtle);
                        break;
                }
            }
        }

        // ����Ѫ��
        marioblood1 = new Blood(10, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        marioblood2 = new Blood(40, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        marioblood3 = new Blood(70, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        marioblood21 = new Blood(760, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        marioblood22 = new Blood(730, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        marioblood23 = new Blood(700, 30, 30, 30, new ImageIcon("image/redheart.jpg").getImage());
        mario.start();
        mario2.start();

        // ��ѭ��
        new Thread(() -> {
            while (true) {
                if (!isPaused) {
                    for (Enemy e : eneryList) {
                        if (e instanceof Turtle) {
                            Turtle turtle = (Turtle) e;
                            turtle.move();
                            turtle.checkCollision(this);
                        }else if (e instanceof Mogu) {
                            Mogu mogu = (Mogu) e;
                            mogu.move();
                            mogu.checkCollision(this);
                        }
                    }
                    repaint();
                    checkBoom();
                    try {
                        checkend();
                        checkmario();
                        checkmario2();
                        checkdistance();
                        changeflag = false;
                        changeflag2 = false;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> MusicUtil.playBackground()).start();

        // ���Ӽ��̼�����
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "ȷ��Ҫ�˳���Ϸ������ѡ��ؿ�������",
                            "�˳���Ϸ",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        endflag = true;
                        dispose();
                        try {
                            ChooseFrame choose = new ChooseFrame(username,userid);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = (BufferedImage) this.createImage(this.getSize().width, this.getSize().height);
        Graphics big = bi.getGraphics();
        big.drawImage(bg.img, bg.x, bg.y, null);

        for (int i = 0; i < eneryList.size(); i++) {
            Enemy e = eneryList.get(i);
            big.drawImage(e.img, e.x, e.y, e.width, e.height, null);
            if (e instanceof Coin) {
                Coin coin = (Coin) e;
                if (coin.isShowSun()) {
                    Image sunImg = new ImageIcon("image/coin.png").getImage();
                    big.drawImage(sunImg, coin.x, coin.y - 30, 30, 30, null);
                }
            }
        }

        for (int i = 0; i < boomList.size(); i++) {
            Boom b = boomList.get(i);
            Color c = big.getColor();
            big.setColor(Color.red);
            big.fillOval(b.x += b.speed, b.y, b.width, b.width);
            big.setColor(c);
        }

        for (int i = 0; i < boomList2.size(); i++) {
            Boom b2 = boomList2.get(i);
            Color c = big.getColor();
            big.setColor(Color.red);
            big.fillOval(b2.x += b2.speed, b2.y, b2.width, b2.width);
            big.setColor(c);
        }

        // ����Ѫ��
        if (mario.blood == 1) {
            big.drawImage(marioblood1.img, marioblood1.x, marioblood1.y, marioblood1.width, marioblood1.height, null);
        } else if (mario.blood == 2) {
            big.drawImage(marioblood1.img, marioblood1.x, marioblood1.y, marioblood1.width, marioblood1.height, null);
            big.drawImage(marioblood2.img, marioblood2.x, marioblood2.y, marioblood2.width, marioblood2.height, null);
        } else if (mario.blood == 3) {
            big.drawImage(marioblood1.img, marioblood1.x, marioblood1.y, marioblood1.width, marioblood1.height, null);
            big.drawImage(marioblood2.img, marioblood2.x, marioblood2.y, marioblood2.width, marioblood2.height, null);
            big.drawImage(marioblood3.img, marioblood3.x, marioblood3.y, marioblood3.width, marioblood3.height, null);
        }

        if (mario2.blood2 == 1) {
            big.drawImage(marioblood21.img, marioblood21.x, marioblood21.y, marioblood21.width, marioblood21.height, null);
        } else if (mario2.blood2 == 2) {
            big.drawImage(marioblood21.img, marioblood21.x, marioblood21.y, marioblood21.width, marioblood21.height, null);
            big.drawImage(marioblood22.img, marioblood22.x, marioblood22.y, marioblood22.width, marioblood22.height, null);
        } else if (mario2.blood2 == 3) {
            big.drawImage(marioblood21.img, marioblood21.x, marioblood21.y, marioblood21.width, marioblood21.height, null);
            big.drawImage(marioblood22.img, marioblood22.x, marioblood22.y, marioblood22.width, marioblood22.height, null);
            big.drawImage(marioblood23.img, marioblood23.x, marioblood23.y, marioblood23.width, marioblood23.height, null);
        }

        // ���Ʒ���
        big.setFont(new Font("Arial", Font.BOLD, 18));
        big.setColor(Color.WHITE);
        big.drawString("1P Score: " + score1, 150, 50);
        big.drawString("2P Score: " + score2, 560, 50);

        // ���Ƴ�������
        big.drawImage(mario.img, mario.x, mario.y, mario.width, mario.height, null);
        big.drawImage(mario2.img2, mario2.x2, mario2.y2, mario2.width2, mario2.height2, null);
        g.drawImage(bi, 0, 0, null);
    }

    // ����ӵ��Ƿ���ײ
    public void checkBoom() {
        for (int i = 0; i < boomList.size(); i++) {
            Boom b = boomList.get(i);
            if (b.x < 0 || b.x > 800) {
                boomList.remove(i);
            }
        }

        for (int i = 0; i < boomList2.size(); i++) {
            Boom b2 = boomList2.get(i);
            if (b2.x < 0 || b2.x > 800) {
                boomList2.remove(i);
            }
        }

        ArrayList<Enemy> toRemove = new ArrayList<>();
        Iterator<Boom>it1=boomList.iterator();
        while(it.hasNext()){
            Boom boom=it1.next();
            for(Enemy e:eneryList){
                if(e instanceof Turtle){
                    Turtle turtle=(Turtle)e;
                    if(!turtle.isDead&&turtle.getBounds().intersects(boom.getBounds())){
                        turtle.isDead=true;
                        toRemove.add(turtle);
                        mario.score+=5;
                        score1=mario.score;
                        it.remove();
                        break;//避免多次伤害
                    }
                }
            }
        }
        // 子弹2（mario2）处理逻辑
        Iterator<Boom> it2 = boomList2.iterator();
        while (it2.hasNext()) {
        Boom boom = it2.next();
            for (Enemy e : eneryList) {
            if (e instanceof Turtle) {
            Turtle turtle = (Turtle) e;
                if (!turtle.isDead && turtle.getBounds().intersects(boom.getBounds())) {
                turtle.isDead = true;
                toRemove.add(turtle);
                mario2.score2 += 5;
                score2 = mario2.score2;
                it2.remove(); // 子弹移除
                break;
                }
            }
        }
    }
        
        // �Ƴ������ĵ���
        for (Enemy enemy : toRemove) {
            eneryList.remove(enemy);
        }
    }

    // ������Ϸ��������ݿ�
    private void saveGameResult(String result, int userid, String username) {
        String sql = "INSERT INTO game_results (userid, username, player1_score, player1_blood, player2_score, player2_blood, result, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, userid); // �û�ID
            pstmt.setString(2, username); // �û���
            pstmt.setInt(3, score1); // ��ɫ1�÷�
            pstmt.setInt(4, mario.blood); // ��ɫ1Ѫ��
            pstmt.setInt(5, score2); // ��ɫ2�÷�
            pstmt.setInt(6, mario2.blood2); // ��ɫ2Ѫ��
            pstmt.setString(7, result); // ��Ϸ��� (Success / Dead)
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // ��ǰʱ��

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.close(conn, pstmt);
        }
    }

    // �����Ϸ�Ƿ����
    public void checkend() throws Exception {
        if (bg.endflag == true) {
            saveGameResult("Success", userid, username);
            saveGameResult(username, mapnum);
            JOptionPane.showMessageDialog(null, "success", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
            bg.endflag = false;
            dispose();
            ChooseFrame choose = new ChooseFrame(username, userid);// �رյ�ǰ���ڲ���ѡ�񴰿�
        }
    }
    public void saveGameResult(String username, int level) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        GameRecordDAO gameRecordDAO = new GameRecordDAO();

        // ��ȡ�û������м�¼
        List<GameRecord> existingRecords = gameRecordDAO.getGameRecordsByUsername(username);

        int totalScore = score1 + score2; // �����ܵ÷�
        int maxBlood = Math.max(mario.blood, mario2.blood2); // �������Ѫ��

        if (existingRecords.isEmpty()) {
            // ���û�����м�¼�������¼�¼
            GameRecord newRecord = new GameRecord(username, totalScore, maxBlood, currentTimestamp, level);
            gameRecordDAO.saveGameRecord(newRecord);
            JOptionPane.showMessageDialog(null, "Game result saved as new record!");
        } else {
            GameRecord existingRecord = existingRecords.get(0);
            int existingLevel = existingRecord.getLevel();

            // ����¹ؿ��������м�¼������¼�¼
            if (level > existingLevel) {
                existingRecord.setScore(totalScore);
                existingRecord.setBlood(maxBlood);
                existingRecord.setCreatedAt(currentTimestamp);
                existingRecord.setLevel(level);
                gameRecordDAO.updateGameRecord(existingRecord); // ���¼�¼
                JOptionPane.showMessageDialog(null, "Level updated to " + level + "!");
            } else if (level == existingLevel) {
                // ����ؿ���ͬ�����÷��Ƿ����
                if (totalScore > existingRecord.getScore()) {
                    existingRecord.setScore(totalScore);
                    existingRecord.setBlood(maxBlood);
                    existingRecord.setCreatedAt(currentTimestamp);
                    gameRecordDAO.updateGameRecord(existingRecord); // ���¼�¼
                    JOptionPane.showMessageDialog(null, "Score updated to " + totalScore + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "No update needed. Existing score is higher or equal.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No update needed. Existing level is higher.");
            }
        }
    }




    // ���������ɫ�ľ���
    public void checkdistance() {
        if ((mario.x - mario2.x2) >= 400) {
            mario.xspeed = 0;
        } else if ((mario2.x2 - mario.x) >= 400) {
            mario2.xspeed2 = 0;
        } else {
            mario.xspeed = 5;
            mario2.xspeed2 = 5;
        }
    }

    public void checkmario() throws Exception {
        for (Enemy e : eneryList) {
            if (e instanceof Turtle) {
                Turtle turtle = (Turtle) e;
                if (mario.x + mario.width > turtle.x && mario.x < turtle.x + turtle.width &&
                        mario.y + mario.height > turtle.y && mario.y < turtle.y + turtle.height) {
                    mario.blood--;
                    if (mario.blood == 0 && !endflag) {
                        saveGameResult("Mario1 is dead", userid, username);
                        saveGameResult(username, mapnum);
                        JOptionPane.showMessageDialog(null, "Mario1 is dead", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
                        endflag = true;
                        dispose();
                        ChooseFrame choose = new ChooseFrame(username, userid);
                    }
                }
            }
        }

        if (hitflag1 && !changeflag) {
            mario.blood--;
            changeflag = true;
            if (mario.right)
                mario.x -= 80;
            else
                mario.x += 80;
            hitflag1 = false;
        }

        int bufferZone = 10;// ��ײ���Ļ�����

        if (mario.x + mario.width > castle.x - bufferZone && mario.x < castle.x + castle.width + bufferZone &&
                mario.y + mario.height > castle.y - bufferZone && mario.y < castle.y + castle.height + bufferZone &&
                mario2.x2 + mario2.width2 >                castle.x - bufferZone && mario2.x2 < castle.x + castle.width + bufferZone &&
                mario2.y2 + mario2.height2 > castle.y - bufferZone && mario2.y2 < castle.y + castle.height + bufferZone) {
            if (!endflag) {
                JOptionPane.showMessageDialog(null, "ͨ�أ�", "��Ϸ��ʾ", JOptionPane.INFORMATION_MESSAGE);
                endflag = true;
                saveGameResult("Success", userid, username);
                dispose();
                ChooseFrame choose = new ChooseFrame(username, userid);
            }
        }
        if (mario.blood == 0 && !endflag) {
            saveGameResult("Mario1 is dead", userid, username);
            JOptionPane.showMessageDialog(null, "Mario1 is dead", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
            endflag = true;
            dispose();
            ChooseFrame choose = new ChooseFrame(username, userid);
        }
    }

    public void checkmario2() throws Exception {
        for (Enemy e : eneryList) {
            if (e instanceof Turtle) {
                Turtle turtle = (Turtle) e;
                if (mario2.x2 + mario2.width2 > turtle.x && mario2.x2 < turtle.x + turtle.width &&
                        mario2.y2 + mario2.height2 > turtle.y && mario2.y2 < turtle.y + turtle.height) {
                    mario2.blood2--;
                    if (mario2.blood2 == 0 && !endflag) {
                        saveGameResult("Mario2 is dead", userid, username);
                        saveGameResult(username, mapnum);
                        JOptionPane.showMessageDialog(null, "Mario2 is dead", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
                        endflag = true;
                        dispose();
                        ChooseFrame choose = new ChooseFrame(username, userid);
                    }
                }
            }
        }

        if (hitflag2 && !changeflag2) {
            mario2.blood2--;
            changeflag2 = true;
            if (mario2.right2)
                mario2.x2 -= 80;
            else
                mario2.x2 += 80;
            hitflag2 = false;
        }

        int bufferZone = 10;

        if (mario.x + mario.width > castle.x - bufferZone && mario.x < castle.x + castle.width + bufferZone &&
                mario.y + mario.height > castle.y - bufferZone && mario.y < castle.y + castle.height + bufferZone &&
                mario2.x2 + mario2.width2 > castle.x - bufferZone && mario2.x2 < castle.x + castle.width + bufferZone &&
                mario2.y2 + mario2.height2 > castle.y - bufferZone && mario2.y2 < castle.y + castle.height + bufferZone) {
            if (!endflag) {
                JOptionPane.showMessageDialog(null, "ͨ�أ�", "��Ϸ��ʾ", JOptionPane.INFORMATION_MESSAGE);
                endflag = true;
                saveGameResult("Success", userid, username);
                dispose();
                ChooseFrame choose = new ChooseFrame(username, userid);
            }
        }
        if (mario2.blood2 == 0 && !endflag) {
            saveGameResult("Mario2 is dead", userid, username);
            JOptionPane.showMessageDialog(null, "Mario2 is dead", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
            endflag = true;
            dispose();
            ChooseFrame choose = new ChooseFrame(username, userid);
        }
    }
}
