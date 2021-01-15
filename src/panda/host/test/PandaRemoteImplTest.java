package panda.host.test;

import org.junit.jupiter.api.Test;
import panda.host.model.models.filters.PostFilter;
import panda.host.utils.Panda;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        PandaOperation pandaOperation = Panda.getOperationTypeFromPandaCode(postRequest);

//        String serverResponse = new PandaRemoteImpl().getPosts(postRequest);

        assertNotNull(pandaOperation);
    }

    @Test
    void addPost() {
    }
}