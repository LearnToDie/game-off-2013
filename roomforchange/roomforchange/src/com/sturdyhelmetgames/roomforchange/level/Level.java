package com.sturdyhelmetgames.roomforchange.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sturdyhelmetgames.roomforchange.assets.Assets;
import com.sturdyhelmetgames.roomforchange.entity.Entity;
import com.sturdyhelmetgames.roomforchange.entity.Player;
import com.sturdyhelmetgames.roomforchange.util.LabyrinthUtil;

public class Level {

	private LabyrinthPiece[][] labyrinth;
	private LevelTile[][] tiles;
	private final Vector2 currentPiecePos = new Vector2();

	public Player player;
	public final Array<Entity> entities = new Array<Entity>();

	public Vector2 findCurrentPiecePos() {
		for (int x = 0; x < labyrinth[0].length; x++) {
			final LabyrinthPiece[] pieceList = labyrinth[x];
			for (int y = 0; y < pieceList.length; y++) {
				final LabyrinthPiece piece = pieceList[y];
				if (player.bounds.overlaps(piece.getBounds())) {
					currentPiecePos.set(x, y);
					return currentPiecePos;
				}
			}
		}
		throw new RuntimeException("No piece found for player");
	}

	public LabyrinthPiece[][] getLabyrinth() {
		return labyrinth;
	}

	public void setLabyrinth(LabyrinthPiece[][] labyrinth) {
		this.labyrinth = labyrinth;
	}

	public LevelTile[][] getTiles() {
		return tiles;
	}

	public void setTiles(LevelTile[][] tiles) {
		this.tiles = tiles;
	}

	public static enum LevelTileType {
		GROUND, WALL, EXIT;

		public boolean isExit() {
			return this == EXIT;
		}

		public boolean isCollidable() {
			return this == WALL;
		}
	}

	public static class LevelTile {
		public final LevelTileType type;

		public LevelTile(LevelTileType type) {
			this.type = type;
		}

		public void render(float delta, SpriteBatch batch, float x, float y) {
			if (type == LevelTileType.GROUND) {
				batch.draw(Assets.getGameObject("ground"), x, y, 1f, 1f);
			} else if (type == LevelTileType.WALL) {
				batch.draw(Assets.getGameObject("brick"), x, y, 1f, 1f);
			}
		}

		public boolean isCollidable() {
			return type.isCollidable();
		}
	}

	public void update(float fixedStep) {
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).update(fixedStep);
		}
	}

	public void render(float delta, SpriteBatch batch) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				final LevelTile tile = tiles[x][y];
				tile.render(delta, batch, x, y);
			}
		}
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).render(delta, batch);
		}
	}

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;

	public void moveLabyrinthPiece(int dir) {
		final Vector2 currentPiecePos = findCurrentPiecePos();
		LabyrinthPiece nextPiece = labyrinth[(int) currentPiecePos.x][(int) currentPiecePos.y];

		switch (dir) {
		case (LEFT): {
			int width = labyrinth[0].length;
			int xPos = (int) currentPiecePos.x - 1;
			int yPos = (int) currentPiecePos.y;
			for (int i = 0; i < width; i++) {
				if (xPos < 0) {
					xPos += width;
				}
				LabyrinthPiece currentPiece = nextPiece;
				nextPiece = labyrinth[xPos][yPos];
				labyrinth[xPos][yPos] = currentPiece;
				xPos -= 1;
			}
			break;
		}
		case (RIGHT): {
			int width = labyrinth[0].length;
			int xPos = (int) currentPiecePos.x + 1;
			int yPos = (int) currentPiecePos.y;
			for (int i = 0; i < width; i++) {
				if (xPos >= width) {
					xPos -= width;
				}
				LabyrinthPiece currentPiece = nextPiece;
				nextPiece = labyrinth[xPos][yPos];
				labyrinth[xPos][yPos] = currentPiece;
				xPos += 1;
			}
			break;
		}
		case (UP): {
			int height = labyrinth.length;
			int xPos = (int) currentPiecePos.x;
			int yPos = (int) currentPiecePos.y + 1;
			for (int i = 0; i < height; i++) {
				if (yPos >= height) {
					yPos -= height;
				}
				LabyrinthPiece currentPiece = nextPiece;
				nextPiece = labyrinth[xPos][yPos];
				labyrinth[xPos][yPos] = currentPiece;
				yPos += 1;
			}
			break;
		}
		case (DOWN): {
			int height = labyrinth.length;
			int xPos = (int) currentPiecePos.x;
			int yPos = (int) currentPiecePos.y - 1;
			for (int i = 0; i < height; i++) {
				if (yPos < 0) {
					yPos += height;
				}
				LabyrinthPiece currentPiece = nextPiece;
				nextPiece = labyrinth[xPos][yPos];
				labyrinth[xPos][yPos] = currentPiece;
				yPos -= 1;
			}
			break;
		}
		}
		LabyrinthUtil.updateLabyrinthTiles(this);
	}
}
