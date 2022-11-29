package edu.lehigh.cse216.jub424.backend.data_manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * ResourceManager contains a function for making requests to the Drive API
 * 
 * @author Ala Chua
 * @version 1.0.0
 * @since 2022-11-16
 */
/* class to demonstarte use of Drive files list API */
// public class GoogleDriveManager {

// public void GoogleDrive(String... args) throws IOException,
// GeneralSecurityException {
// // Build a new authorized API client service.
// final NetHttpTransport HTTP_TRANSPORT =
// GoogleNetHttpTransport.newTrustedTransport();
// Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
// getCredentials(HTTP_TRANSPORT))
// .setApplicationName(APPLICATION_NAME).build();

// // Print the names and IDs for up to 10 files.
// FileList result =
// service.files().list().setPageSize(10).setFields("nextPageToken, files(id,
// name)").execute();
// List<File> files = result.getFiles();
// if (files == null || files.isEmpty()) {
// System.out.println("No files found.");
// } else {
// System.out.println("Files:");
// for (File file : files) {
// System.out.printf("%s (%s)\n", file.getName(), file.getId());
// }
// }
// }
public class GoogleDriveManager {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Google Drive API Java";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  // private static final List<String> SCOPES =
  // Collections.singletonList(DriveScopes.DRIVE_FILE);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws Exception
   */
  // private static GoogleCredentials getCredentials(final NetHttpTransport
  // HTTP_TRANSPORT) throws IOException {
  // // Load client secrets.
  // InputStream in =
  // GoogleDriveManager.class.getClass().getResourceAsStream(CREDENTIALS_FILE_PATH);
  // if (in == null) {
  // throw new FileNotFoundException("Resource not found: " +
  // CREDENTIALS_FILE_PATH);
  // }
  // GoogleCredentials credential =
  // GoogleCredentials.fromStream(in).createScoped(SCOPES);
  // // returns an authorized Credential object.
  // return credential;
  // }

  // /**
  // * Creates an authorized Credential object.
  // *
  // * @param HTTP_TRANSPORT The network HTTP Transport.
  // * @return An authorized Credential object.
  // * @throws IOException If the credentials.json file cannot be found.
  // */
  private static GoogleCredentials getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
    // Load client secrets.
    Map<String, String> env = System.getenv();
    if (env.get("CREDENTIALS_KEY") == null) {
      throw new Exception("Cannot load credentials key");
    }
    InputStream in = GoogleDriveManager.class.getClass().getResourceAsStream(env.get("CREDENTIALS_KEY"));
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    // GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
    // new InputStreamReader(in));

    // // Build flow and trigger user authorization request.
    // GoogleAuthorizationCodeFlow flow = new
    // GoogleAuthorizationCodeFlow.Builder(
    // HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
    // .setDataStoreFactory(new FileDataStoreFactory(new
    // java.io.File(TOKENS_DIRECTORY_PATH)))
    // .setAccessType("offline")
    // .build();
    // LocalServerReceiver receiver = new
    // LocalServerReceiver.Builder().setPort(8888).build();
    // Credential credential = new AuthorizationCodeInstalledApp(flow,
    // receiver).authorize("user");
    GoogleCredentials credential = GoogleCredentials.fromStream(in).createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    System.out.println("Credential: " + credential);
    // returns an authorized Credential object.
    return credential;
  }

  public static void quickStart() throws IOException, GeneralSecurityException {
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    try {
      GoogleCredentials credentials = getCredentials(HTTP_TRANSPORT);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
    // (HttpRequestInitializer) getCredentials(HTTP_TRANSPORT))
    // .setApplicationName(APPLICATION_NAME)
    // .build();
    // // Print the names and IDs for up to 10 files.
    // FileList result = service.files().list()
    // .setPageSize(10)
    // .setFields("nextPageToken, files(id, name)")
    // .execute();
    // List<File> files = result.getFiles();
    // if (files == null || files.isEmpty()) {
    // System.out.println("No files found.");
    // } else {
    // System.out.println("Files:");
    // for (File file : files) {
    // System.out.printf("%s (%s)\n", file.getName(), file.getId());
    // }
    // }
  }

  /**
   * Upload new file.
   * 
   * @param res The resource object
   *
   * @return Inserted file metadata if successful, {@code null} otherwise.
   * @throws Exception
   */
  public static String uploadBasic(Resource res) throws Exception {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    try {
      // Load pre-authorized user credentials from the environment.
      // TODO(developer) - See https://developers.google.com/identity for
      // guides on implementing OAuth2 for your application.
      // GoogleCredentials credential =
      // GoogleCredentials.fromStream(credentials).createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
      GoogleCredentials credentials = getCredentials(HTTP_TRANSPORT);
      HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
      // Build a new authorized API client service.
      Drive service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
          .setApplicationName(APPLICATION_NAME).build();
      // Upload file photo.jpg on drive.
      File fileMetadata = new File();
      fileMetadata.setName(getFilename(res.link));
      // File's content.
      java.io.File filePath = new java.io.File(res.link);
      // Specify media type and file-path for file.
      FileContent mediaContent = new FileContent(getFileType(res.link), filePath);
      try {
        File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        System.out.println("File ID: " + file.getId());
        return file.getId();
      } catch (GoogleJsonResponseException e) {
        // TODO(developer) - handle error appropriately
        System.err.println("Unable to upload file: " + e.getDetails());
        throw e;
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "Error";
  }
  // /**
  // * @param res The resource
  // * @return String file ID
  // * @throws IOException
  // */
  // public String uploadSimple(Resource res) throws IOException {
  // InputStream in =
  // GoogleDriveManager.class.getClass().getResourceAsStream(CREDENTIALS_FILE_PATH);
  // if (in == null) {
  // throw new FileNotFoundException("Resource not found: " +
  // CREDENTIALS_FILE_PATH);
  // }
  // GoogleCredentials credentials =
  // GoogleCredentials.fromStream(in).createScoped(SCOPES);
  // System.out.println("Credential: " + credentials);
  // // GoogleCredentials credentials =
  // //
  // GoogleCredentials.getApplicationDefault().createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
  // HttpRequestInitializer requestInitializer = new
  // HttpCredentialsAdapter(credentials);

  // // Build a new authorized API client service.
  // Drive service = new Drive.Builder(new NetHttpTransport(),
  // GsonFactory.getDefaultInstance(), requestInitializer)
  // .setApplicationName(APPLICATION_NAME).build();
  // File fileMetadata = new File();
  // fileMetadata.setName(getFilename(res.link));
  // fileMetadata.setMimeType(null);

  // java.io.File filePath = new java.io.File(res.link);
  // FileContent mediaContent = new FileContent(getFileType(res.link), filePath);
  // File file = service.files().create(fileMetadata, mediaContent)
  // .setFields("id")
  // .execute();
  // return file.getId();
  // }

  /**
   * getFilename is a helper function for getting the name of a file
   * 
   * @param link
   * @return
   */
  public static String getFilename(String link) {
    int count = 0;
    for (int i = 0; i < link.length(); i++) {
      if (link.charAt(i) == '/')
        count++;
    }
    String[] name = link.split("/");
    return name[count];
  }

  public static String getFileType(String link) {
    int count = 0;
    for (int i = 0; i < link.length(); i++) {
      if (link.charAt(i) == '/')
        count++;
    }
    String[] name = link.split("/");
    String file = name[count];
    return (file.split("."))[1];
  }
}