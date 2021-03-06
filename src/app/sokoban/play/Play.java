package app.sokoban.play;

import app.sokoban.io.CmdStageWriter;
import app.sokoban.io.StageWriter;
import app.sokoban.meta.Sign;
import app.sokoban.play.command.PlayerCommand;
import app.sokoban.play.command.SystemCommand;
import app.sokoban.play.point.Point;
import app.sokoban.play.point.Position;
import app.sokoban.play.store.PlayStatusStore;

import java.util.Optional;
import java.util.Scanner;

public class Play {
    private Stage stage;
    private StageWriter writer;
    private PlayStatus playStatus;
    private PlayStatusStore store;

    public Play(Stage stage) {
        this.stage = stage;
        writer = new CmdStageWriter();
        store = new PlayStatusStore();
        init();
    }

    private void init() {
        playStatus = new PlayStatus.PlayStatusBuilder()
                .setStage(stage)
                .setPlayer(new Position(stage.getPlayerLocation()))
                .setPlayingMap(stage.getCloneChrMap())
                .setPlayerMoveCount(0)
                .setSuccess(false)
                .setQuit(false)
                .build();
    }

    private void init(PlayStatus status) {
        playStatus = new PlayStatus.PlayStatusBuilder()
                .setStage(status.getStage())
                .setPlayer(status.getPlayer())
                .setPlayingMap(status.getClonePlayingMap())
                .setPlayerMoveCount(status.getPlayerMoveCount())
                .setSuccess(status.isSuccess())
                .setQuit(status.isQuit())
                .build();
    }

    private boolean isBall(char chr) {
        return Sign.BALL.getMean() == chr;
    }

    private boolean isPlayer(char chr) {
        return Sign.PLAYER.getMean() == chr;
    }

    private boolean isBallInHall(char chr) {
        return Sign.BALL_IN_HALL.getMean() == chr;
    }

    private Point getNext(Point point, Point direction) {
        int x = point.getX() + direction.getX();
        int y = point.getY() + direction.getY();

        return new Point(x, y);
    }

    public void start() {
        try {
            Scanner sc = new Scanner(System.in);

            writer.writeStage(stage);

            while (!playStatus.isQuit() && !playStatus.isSuccess()) {
                char[] commands = getUserCommands(sc);
                executeAllCommands(commands);
            }

            if (playStatus.isQuit()) System.out.println("Bye~");
            if (playStatus.isSuccess()) System.out.println("?????? ?????? : " + playStatus.getPlayerMoveCount() + "\n??????!! ???????????????.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException("?????? ?????? ??? ????????? ?????????????????????.");
        }
    }

    private char[] getUserCommands(Scanner sc) {
        String line = "";

        System.out.print("SOKOBAN> ");
        if (sc.hasNext()) {
            line = sc.next();
        }
        return line.toCharArray();
    }

    private void executeAllCommands(char[] commands) throws Exception {
        StringBuffer digitBuffer = new StringBuffer();

        for (int i=0; i<commands.length && !playStatus.isQuit(); i++) {
            String command = String.valueOf(commands[i]);

            if(Character.isDigit(commands[i])) {
                digitBuffer.append(commands[i]);
                continue;
            }

            if (SystemCommand.isValidCommand(command)) executeSystemCommand(command, digitBuffer);
            if (playStatus.isQuit()) break;
            if (PlayerCommand.isValidCommand(command)) executePlayerCommand(command);
            if (playStatus.isSuccess()) break;

            if (!isValidCommand(command)) printWarning();
            if (digitBuffer.length() > 0) digitBuffer = new StringBuffer();
        }
    }

    private boolean isValidCommand(String command) {
        return SystemCommand.isValidCommand(command) || PlayerCommand.isValidCommand(command);
    }

    private void executeSystemCommand(String command, StringBuffer digitBuffer) throws Exception {
        if (SystemCommand.isQuit(command)) {
            playStatus.setQuit(true);
            System.out.println(SystemCommand.q.getMessage());
        }

        if (SystemCommand.isReset(command)) {
            reset();
        }

        if (SystemCommand.isSaveOrLoad(command) && digitBuffer.length() > 0) {
            int slotNum = Integer.parseInt(digitBuffer.toString());
            saveOrLoad(command, slotNum);
        }

        if (SystemCommand.isUndoOrCancelUndo(command)) {
            UndoOrCancelUndo(command);
        }
    }

    private void saveOrLoad(String command, int slotNum) throws Exception {
        SystemCommand.findSystemCommand(command).ifPresent((systemCommand) -> {
            System.out.println(slotNum + "??? " + systemCommand.getMessage());

            if (!store.isAvailableSlot(slotNum)) {
                System.out.println("????????? ??? ?????? ???????????????.");
                return;
            }

            if (SystemCommand.isSave(command) && store.saveStatus(playStatus, slotNum)) System.out.println("?????? ??????!");

            if (SystemCommand.isLoad(command)) {
                store.loadStatus(slotNum)
                        .ifPresentOrElse(this::init, () -> System.out.println("??????????????? ???????????? ??????????????????."));
            }
        });

        playStatus.printPlayingMap();
    }

    private void UndoOrCancelUndo(String command) {
        boolean isStackEmpty = false;

        if (SystemCommand.isUndo(command)) {
            if (!(isStackEmpty = playStatus.isDoStackEmpty())) {
                Turn turn = playStatus.popDoStack();
                undoMoveProcess(turn);
            }
        }

        if (SystemCommand.isCancelUndo(command)) {
            if (!(isStackEmpty = playStatus.isUndoStackEmpty())) {
                Turn turn = playStatus.popUndoStack();
                moveProcess(turn.getCommand());
            }
        }

        if (isStackEmpty) printWarning();
    }

    private void reset() throws Exception{
        System.out.println(SystemCommand.r.getMessage());
        init();
        playStatus.printPlayingMap();
    }

    private void executePlayerCommand(String command) {
        System.out.println("\n?????????: " + command);
        PlayerCommand.findPlayerCommand(command)
                .ifPresent(this::moveProcess);
        playStatus.clearUndoStack();
    }

    private void moveProcess(PlayerCommand playerCommand) {
        Point playerNextStep = getNext(playStatus.getPlayer(), playerCommand.getDirection());
        char next = playStatus.getValueOfPlayingMap(playerNextStep);
        boolean nextIsBall = (isBall(next) || isBallInHall(next));

        if (nextIsBall) {
            moveBallAndPlayer(playerNextStep, playerCommand);
        } else {
            Optional<Point> movedPlayer = tryMovePlayer(playerCommand);

            if (movedPlayer.isPresent()) {
                Turn turn = new Turn(movedPlayer.get(), playerCommand);

                setMovedTurn(turn);
            }
        }
    }

    private void moveBallAndPlayer(Point playerNextStep, PlayerCommand playerCommand) {
        Optional<Point> movedBall = tryMoveBall(playerNextStep, playerCommand);

        if (movedBall.isPresent()) {
            Optional<Point> movedPlayer = tryMovePlayer(playerCommand);

            if (movedPlayer.isPresent()) {
                Turn turn = new Turn(movedPlayer.get(),movedBall.get(), playerCommand);

                setMovedTurn(turn);
            }
        }
    }

    private void setMovedTurn(Turn turn) {
        playStatus.pushDoStack(turn);
    }

    private Optional<Point> tryMoveBall(Point ball, PlayerCommand playerCommand) {
        Point movedBall;
        Point direction = playerCommand.getDirection();

        if (!isBallMoveable(ball, direction)) {
            printWarning();
            return Optional.empty();
        }

        movedBall = moveBallPosition(ball, direction);

        return Optional.of(movedBall);
    }

    private Point moveBallPosition(Point ball, Point direction) {
        Point movedBall = getNext(ball, direction);

        char origin = stage.getOriginValueOfChrMap(ball);       // BALL??? ?????? ????????? ?????? ???
        char next = playStatus.getValueOfPlayingMap(movedBall); // BALL??? ????????? ????????? ?????? ???
        char nextValue = Sign.BALL.getMean();                   // BALL??? ????????? ????????? ????????? ???

        if (isBall(origin) || isPlayer(origin)) origin = Sign.EMPTY.getMean();
        if (Sign.HALL.getMean() == next) nextValue = Sign.BALL_IN_HALL.getMean();

        playStatus.setValueOnPlayingMap(ball, origin);
        playStatus.setValueOnPlayingMap(movedBall, nextValue);

        return movedBall;
    }

    private boolean isBallMoveable(Point position, Point direction) {
        Point nextStep = getNext(position, direction);

        if (playStatus.isOutOfBoundOnPlayingMap(nextStep)) return false;

        char next = playStatus.getValueOfPlayingMap(nextStep);

        //EMPTY, HALL ??? ????????? ????????? ??? ??????
        if (Sign.EMPTY.getMean() != next &&
                Sign.HALL.getMean() != next ) return false;

        return true;
    }

    private Optional<Point> tryMovePlayer(PlayerCommand playerCommand) {
        try {
            Point direction = playerCommand.getDirection();
            if (!isPlayerMoveable(playStatus.getPlayer(), direction)) {
                printWarning();
                return Optional.empty();
            }

            Point movedPlayer = movePlayerPosition(playStatus.getPlayer(), playerCommand.getDirection());
            playStatus.playerMoved();
            if (playStatus.checkSuccess()) playStatus.setSuccess(true);

            System.out.println(playerCommand.name() + ": " + playerCommand.getMessage());
            playStatus.printPlayingMap();

            return Optional.of(movedPlayer);

        } catch (Exception e) {
            throw new IllegalStateException("??????????????? ???????????? ?????? ????????? ?????????????????????.[" + playerCommand.name() + "]");
        }
    }

    private boolean isPlayerMoveable(Position position, Point direction) {
        Point nextStep = getNext(position, direction);

        if (playStatus.isOutOfBoundOnPlayingMap(nextStep)) return false;

        char next = playStatus.getValueOfPlayingMap(nextStep);

        //EMPTY, HALL, BALL_IN_HALL ??? ????????? ????????? ??? ??????
        if (Sign.EMPTY.getMean() != next &&
                Sign.HALL.getMean() != next &&
                Sign.BALL_IN_HALL.getMean() != next ) return false;

        return true;
    }

    private Point movePlayerPosition(Point player, Point direction) {
        Point nextStep = getNext(player, direction);
        char origin = stage.getOriginValueOfChrMap(player); // PLAYER??? ?????? ????????? ?????? ???

        if (Sign.PLAYER.getMean() == origin ||
                Sign.BALL.getMean() == origin) origin = Sign.EMPTY.getMean();

        playStatus.setValueOnPlayingMap(nextStep, Sign.PLAYER.getMean());
        playStatus.setValueOnPlayingMap(player, origin);

        playStatus.getPlayer().x = nextStep.getX();
        playStatus.getPlayer().y = nextStep.getY();

        return nextStep;
    }

    private void undoMoveProcess(Turn turn) {

        try {
            Point reverse = turn.getCommand().getReverse();
            movePlayerPosition(turn.getMovedPlayer(), reverse);

            if (turn.isBallMoved()) {
                moveBallPosition(turn.getMovedBall(), reverse);
            }

            playStatus.pushUndoStack(turn);

            System.out.println("??? ??? ????????????: " + turn.getCommand().name());

            playStatus.printPlayingMap();
        } catch (Exception e) {
            throw new IllegalStateException("??? ??? ???????????? ?????? ??? ????????? ?????????????????????.[" + turn.getCommand().name() + "]");
        }
    }

    private void printWarning() {
        try {
            System.out.println("(??????!) ?????? ????????? ????????? ??? ????????????!!");
            playStatus.printPlayingMap();
        } catch (Exception e) {
            throw new IllegalStateException("?????? ????????? ?????? ??? ????????? ?????????????????????.");
        }
    }

    public boolean isSuccess() {
        return playStatus.isSuccess();
    }

    public Stage getStage() {
        return stage;
    }
}