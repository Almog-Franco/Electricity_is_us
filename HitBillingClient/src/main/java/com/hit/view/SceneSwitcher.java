package com.hit.view;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface SceneSwitcher {
    public void changeScene(ActionEvent event,String sceneName) throws IOException;
    public void onBackButtonClick(ActionEvent event) throws IOException;
}
