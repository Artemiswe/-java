package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图配置类，用于读取地图配置文件并将数据存储到二维数组中。
 */
public class Map {
    // 数据容器，用于存储地图配置中的每一行数据（字符串形式）。
    public List<String> list = new ArrayList<>();

    // 二维数组，用于存储地图的行列矩阵（整数形式）。
    public int[][] map = null;

    // 文件相关对象，用于读取地图文件。
    public File myfile;
    public FileInputStream fis;
    public InputStreamReader isr;
    public BufferedReader br;

    /**
     * 读取地图文件并将其转换为二维数组。
     *
     * @param num 地图文件编号，决定读取哪个地图文件。
     * @return 地图的二维整数数组。
     * @throws Exception 如果在读取文件时发生错误。
     */
    public int[][] readMap(int num) throws Exception {
        // 根据地图编号选择要读取的文件
        if (num == 1) {
            myfile = new File("map1.txt");
        } else if (num == 2) {
            myfile = new File("map2.txt");
        } else if (num == 3) {
            myfile = new File("map3.txt");
        } else if (num == 4) {
            myfile = new File("map4.txt");
        } else if (num == 5) {
            myfile = new File("map5.txt");
        }

        // 创建文件输入流和相关的读取器
        FileInputStream fis = new FileInputStream(myfile);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        // 逐行读取文件内容
        String value = br.readLine();
        while (value != null) {
            // 将读取到的一行数据加入到容器中
            list.add(value);
            value = br.readLine();
        }

        // 关闭读取器
        br.close();

        // 获取地图的行数
        int row = list.size();

        // 获取地图的列数（假定所有行的列数一致）
        int column = 0;
        for (int i = 0; i < 1; i++) {
            String str = list.get(i);
            String[] values = str.split(",");
            column = values.length;
        }

        // 初始化二维数组的大小
        map = new int[row][column];

        // 将读取到的字符串数据转换为整数，并赋值到二维数组中
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] values = str.split(",");
            for (int j = 0; j < values.length; j++) {
                map[i][j] = Integer.parseInt(values[j]);
            }
        }

        // 返回填充好的地图数组
        return map;
    }
}
