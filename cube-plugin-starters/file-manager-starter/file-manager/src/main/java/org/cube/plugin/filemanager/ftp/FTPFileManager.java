package org.cube.plugin.filemanager.ftp;

import cn.hutool.core.io.IoUtil;
import org.cube.plugin.filemanager.IFileManager;
import org.cube.plugin.filemanager.models.FileServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.Map;

@Slf4j
public class FTPFileManager implements IFileManager<FTPClient> {
    private final FileServer fileServer;
    private FTPClient ftpClient;

    public FTPFileManager(FileServer fileServer) {
        this.fileServer = fileServer;
    }

    @Override
    @SneakyThrows
    public FTPClient defaultClient() {
        if (ftpClient == null) {
            ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(30000);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.enterLocalPassiveMode();
            ftpClient.connect(fileServer.getHostname(), fileServer.getPort());
            ftpClient.login(fileServer.getAccessKey(), fileServer.getSecretKey());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        }
        return ftpClient;
    }

    @Override
    @SneakyThrows
    public void close() {
        ftpClient.disconnect();
        ftpClient = null;
    }

    @Override
    public void upload(String key, File file, Map<String, String> otherParams) {
        upload(key, file);
    }

    @Override
    @SneakyThrows
    public void upload(String key, File file) {
        key = convertPath(key);
        FTPClient client = defaultClient();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String dir = key.substring(0, key.lastIndexOf("/"));
            boolean result = client.makeDirectory(dir);
            log.info("make directory({}) {}", dir, result);
            result = client.storeFile(key, fileInputStream);
            if (!result) {
                log.error("请检查FTP服务器连接是否正常！");
            }
            this.close();
        }
    }

    @Override
    public void upload(String key, byte[] bytes, Map<String, String> otherParams) {
        upload(key, bytes);
    }

    @Override
    @SneakyThrows
    public void upload(String key, byte[] bytes) {
        key = convertPath(key);
        FTPClient client = defaultClient();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            String dir = key.substring(0, key.lastIndexOf("/"));
            boolean result = client.makeDirectory(dir);
            log.info("make directory({}) {}", dir, result);
            result = client.storeFile(key, byteArrayInputStream);
            if (!result) {
                log.error("请检查FTP服务器连接是否正常！");
            }
            this.close();
        }
    }

    @Override
    @SneakyThrows
    public void download(String key, OutputStream outputStream, Map<String, String> otherParams) {
        key = convertPath(key);
        FTPClient client = defaultClient();
        String[] files = client.listNames(key);
        if (files.length > 0) {
            InputStream inputStream = client.retrieveFileStream(key);
            IoUtil.copy(inputStream, outputStream);
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
            this.close();
        }
    }

    /**
     * 转换路径：FTP需要以【/】开始
     *
     * @param path 源路径
     */
    private String convertPath(String path) {
        if (path.startsWith("/")) {
            return path;
        }
        return "/" + path;
    }
}
