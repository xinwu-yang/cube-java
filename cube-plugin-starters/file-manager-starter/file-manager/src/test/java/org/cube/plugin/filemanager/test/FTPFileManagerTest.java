package org.cube.plugin.filemanager.test;

import cn.hutool.core.io.file.FileReader;
import org.cube.plugin.filemanager.IFileManager;
import org.cube.plugin.filemanager.ftp.FTPFileManager;
import org.cube.plugin.filemanager.models.FileServer;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FTPFileManagerTest {
    private IFileManager<FTPClient> fileManager;

    @BeforeEach
    public void before() {
        FileServer ftpServer = FileServer.builder().hostname("25.30.10.224").port(21).accessKey("org/cube/plugin/filemanager/test").secretKey("org/cube/plugin/filemanager/test").build();
        fileManager = new FTPFileManager(ftpServer);
    }

    @Test
    public void upload() {
        fileManager.upload("App/Go2Shell2.dmg", new File("/Users/xinwuy/Downloads/Go2Shell.dmg"));
    }

    @Test
    public void uploadBytes() throws IOException {
        FileReader fileReader = new FileReader("C:\\Users\\xinwuy\\Documents\\vis_api_group.sql");
        fileManager.upload("App/Go2Shell2.dmg", fileReader.readBytes());
    }

    @Test
    public void download() throws FileNotFoundException {
        fileManager.download("App/HBuilderX.2.8.13.dmg", new FileOutputStream("/Users/xinwuy/Downloads/HBuilderX.2.8.13.test.dmg"), null);
    }

    @Test
    public void testDir() {
        String key = "App/mac_osx/HBuilderX.2.8.13.dmg";
        String dir = key.substring(0, key.lastIndexOf("/"));
        System.out.println("dir = " + dir);
    }
}
