package com.ly.mina.selector;


import java.io.IOException;
import java.nio.channels.Selector;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class SelectorTest   extends Application{
	
	Selector selector ;
	
	{
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		FlowPane pane  = new FlowPane();
		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		
		Button selectBtn = new Button("selectBtn");
		
		selectBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					System.out.println("start select ");
					
					
					new Thread(){
						public void run() {
							int select = 0;
							try {
								select = selector.select();
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println("select end : select nums = "+select);
						};
					}.start();
					
			}
		});
		
		Button wakeupBtn = new Button("wakeupBtn");
		wakeupBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Start wakeup btn");
				selector.wakeup();
				System.out.println("end wakup btn");
			}
		});
		
		pane.getChildren().add(selectBtn);
		pane.getChildren().add(wakeupBtn);
		
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
	
