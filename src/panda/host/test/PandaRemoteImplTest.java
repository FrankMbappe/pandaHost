package panda.host.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import panda.host.model.data.PostData;
import panda.host.model.models.Authentication;
import panda.host.model.models.Post;
import panda.host.model.models.User;
import panda.host.model.models.filters.PostFilter;
import panda.host.server.PandaRemoteImpl;
import panda.host.utils.Panda;

import java.io.File;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static panda.host.utils.Panda.PANDA_ENCODING_PATTERNS;
import static panda.host.utils.Panda.PandaOperation;

class PandaRemoteImplTest {

    @Test
    void getPosts() {
        // I create a PostFilter to add filters in my request
        PostFilter filter = new PostFilter(true, "pdf", "3iac_tic_pam_2");
        // Creating the panda code to request the posts list
        String postRequest = String.format(PANDA_ENCODING_PATTERNS.get(PandaOperation.PANDAOP_REQUEST_GET_POSTS),
                filter.isAll(), filter.getFileType(), filter.getSchoolClassId());
        System.out.println("Panda code: " + postRequest);

        // @DEPRECATED
        //PandaOperation pandaOperation = Panda.getOperationTypeFromPandaCode(postRequest);

//        String serverResponse = new PandaRemoteImpl().getPosts(postRequest);

        //assertNotNull(pandaOperation);
    }

    @Test
    void addPost() {
//        Post post = new Post()
        File file = new File("C:/Users/hp/Documents/sd.pdf");
        Post post = new Post("mbappe.frank18@myiuc.com", "Hello world", "seas;btech;swe", file);

        System.out.println("File name: " + post.getFileName());
        System.out.println("File extension: " + post.getFileExt());
        System.out.println("File size: " + Panda.convertLongSizeToString(post.getFileSize()));

        String postToJson = new Gson().toJson(post);

        boolean postHasBeenAdded = false;
        try {
            postHasBeenAdded = new PandaRemoteImpl().addPost(postToJson);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

//        MySQLConnection connection = new MySQLConnection(Objects.requireNonNull(Configs.getMySQLConfig()));

        assertNotEquals(0, new PostData().getAll().size());

        assertTrue(postHasBeenAdded);
    }

    @Test
    void downloadPostFile(){
        byte[] fileToBytes = new byte[0];
        try{
            fileToBytes = new PandaRemoteImpl().downloadPostFile(
                    "mbappe.frank18@myiuc.com",
                    "dd3f8069-f097-4dbc-9953-881dbff6a2cf",
                    "pdf");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        assertNotNull(fileToBytes);
        System.out.println("File downloaded length: " + fileToBytes.length);
    }

    @Test
    void logUserIn(){
        Authentication authProvided = null;
        try{
            User user = new User("mbappe.frank18@myiuc.com","IUC18E0036468");
            String logUserInRequestFormat = Panda.PANDA_ENCODING_PATTERNS.get(PandaOperation.PANDAOP_REQUEST_GET_CONNECTION);
            String pandaCode = String.format(logUserInRequestFormat, user.getUsername(), user.getPassword());

            String authToJson = new PandaRemoteImpl().logUserIn(pandaCode);

            authProvided = new Gson().fromJson(authToJson, Authentication.class);

            System.out.println("Authentication provided: " + authProvided.toString());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        assertNotNull(authProvided);
    }

}