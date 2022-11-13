package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import sample.model.Album;
import sample.model.Artist;
import sample.model.Datasource;

public class Controller {

    @FXML
    private TableView artistTable;
    @FXML
    private ProgressBar progressBar;


    @FXML
    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        /* Bind the artistTable's (TableView) items property to task's (ObservableList<Artist>) value property.  */
        artistTable.itemsProperty().bind(task.valueProperty());

        /* Bind progressBar's (ProgressBar) progress property to task's (Task<ObservableList<Artist>> progress property.  */
        progressBar.progressProperty().bind(task.progressProperty());

        progressBar.setVisible(true);

        /* Set the progressBar invisible if the task succeeds.  */
        task.setOnSucceeded(e -> progressBar.setVisible(false));
        /* Set the progressBar invisible if task fails.  */
        task.setOnFailed(e -> progressBar.setVisible(false));

        /* Kick off the task.  */
        new Thread(task).start();
    }


    @FXML
    public void listAlbumsForArtist() {
        /* Get the selected item from artistTable (TableView<Artist>).  */
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if(artist == null) {
            System.out.println("No artist selected.  ");
            return;
        }
        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            /* Retrieve List<Album> by invoking queryAlbumForArtistId method, pass in artist ID as argument.  */
            @Override
            protected ObservableList<Album> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().queryAlbumForArtistId((artist.getId())));
            }
        };

        /* Bind artistTable's (TableView) to task's (Task<ObservableList<Album>>) value property.  */
        artistTable.itemsProperty().bind(task.valueProperty());

        /* Start the task.  */
        new Thread(task).start();
    }


    @FXML
    public void updateArtist() {
        /* Get the artist from the artistTable.  "AC/DC" is the third element (index 2) of in the artistTable.  */
        final Artist artist = (Artist) artistTable.getItems().get(2);

        Task<Boolean> task = new Task<Boolean>() {
            /* Call the updateArtistName method from Datasource class, pass in artist ID and "AC/DC" as arguments.  */
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().updateArtistName(artist.getId(), "AC/DC");
            }
        };

        task.setOnSucceeded(e -> {
            /* If updateArtistName() returns true, set the artist's name to "AC/DC".  */
            if(task.valueProperty().get()) {
                artist.setName("AC/DC");
                /* Refresh the artistTable (TableView).  */
            }
        });

        /* Start the task.  */
        new Thread(task).start();
    }

}


class GetAllArtistsTask extends Task {

    /* Retrieve a list of artists by ascending order.  */
    @Override
    public ObservableList<Artist> call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }

}