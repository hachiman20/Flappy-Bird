package com.anurag.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import javax.swing.text.View;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	int ind = 0;


	Texture[] birds;
	int flapState = 0;
	int f = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	float gravity = 0.7f;

	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");

		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 175;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startGame();

	}


	public void startGame()
	{
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		for(int i = 0; i < numberOfTubes; i++)
		{
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 350);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i*distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState == 1) {

			if (tubeX[scoringTube] < (Gdx.graphics.getWidth()/2 - topTube.getWidth())) {
				score++;

				if (scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = -17;
			}

			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 350);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() +tubeOffset[i], topTube.getWidth(), topTube.getHeight());

			}

			Gdx.app.log("lol", " 1");

			if (birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
				if(birdY > Gdx.graphics.getHeight() - 140) {
					birdY = Gdx.graphics.getHeight() - 140;
					velocity = 0;
				}
				if(birdY < 0)
					birdY = 0;
			}
			else
			{
				Gdx.app.log("lol", " 2");
				gameState = 2;
			}

			f++;
			if(f<=7)
				flapState = 0;
			else if(f>7)
				flapState=1;
			if(f>14)
				f=0;

			batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY, 150, 140);


		}

		else if(gameState == 0)
		{
			if(Gdx.input.isTouched()) {
				gameState = 1;
			}
			batch.draw(birds[1], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY, 150, 140);
		}
		else if(gameState == 2)
		{
			try {
				ind = scoringTube;
				if (ind == 0 && score == 0) {
					batch.draw(topTube, tubeX[ind], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[ind]);
					batch.draw(bottomTube, tubeX[ind], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[ind]);
				}
				if (ind == 0 && score > 0) {
					batch.draw(topTube, tubeX[ind], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[ind]);
					batch.draw(bottomTube, tubeX[ind], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[ind]);

					batch.draw(topTube, tubeX[3], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[3]);
					batch.draw(bottomTube, tubeX[3], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[3]);
				} else {
					batch.draw(topTube, tubeX[ind], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[ind]);
					batch.draw(bottomTube, tubeX[ind], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[ind]);

					batch.draw(topTube, tubeX[ind - 1], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[ind - 1]);
					batch.draw(bottomTube, tubeX[ind - 1], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[ind - 1]);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			batch.draw(birds[1], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY, 150, 140);
			batch.draw(gameover, Gdx.graphics.getWidth()/2 - 350, Gdx.graphics.getHeight()/2 - 90, 700, 180);


			if(Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}

		}

		font.draw(batch, String.valueOf(score), 100, 200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getWidth	()/2, birds[flapState].getWidth()/2);

		for(int i = 0; i < numberOfTubes; i++) {

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = 2;
			}
		}
		batch.end();
	}
}