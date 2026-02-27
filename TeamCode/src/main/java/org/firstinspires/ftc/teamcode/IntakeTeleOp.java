package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="IntakeTeleOp")
public class IntakeTeleOp extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor intakeMotor;
    private Servo storageServo;
    private DcMotor shooterMotor1;
    private DcMotor shooterMotor2;

    @Override
    public void runOpMode() {

        initHardware();

        waitForStart();

        while (opModeIsActive()) {

            drive();
            intakeControl();
            shooterControl();
            storageControl();

            telemetry.update();
        }
    }

    // ===== INITIALIZATION =====
    private void initHardware() {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        shooterMotor1 = hardwareMap.get(DcMotor.class, "shooterMotor1");
        shooterMotor2 = hardwareMap.get(DcMotor.class, "shooterMotor2");

        storageServo = hardwareMap.get(Servo.class, "storageServo");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        shooterMotor2.setDirection(DcMotor.Direction.REVERSE);
    }

    // ===== DRIVE =====
    private void drive() {

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        double max = Math.max(
                Math.max(Math.abs(fl), Math.abs(bl)),
                Math.max(Math.abs(fr), Math.abs(br))
        );

        if (max > 1.0) {
            fl /= max;
            bl /= max;
            fr /= max;
            br /= max;
        }

        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }

    // ===== INTAKE =====
    private void intakeControl() {
        double intakePower = gamepad1.right_trigger - gamepad1.left_trigger;
        intakeMotor.setPower(intakePower);
    }

    // ===== SHOOTER =====
    private void shooterControl() {

        if (gamepad1.x) {
            shooterMotor1.setPower(1.0);
            shooterMotor2.setPower(1.0);
        }

        if (gamepad1.y) {
            shooterMotor1.setPower(0);
            shooterMotor2.setPower(0);
        }

        telemetry.addData("Shooter Power", shooterMotor1.getPower());
    }

    // ===== STORAGE =====
    private void storageControl() {

        // A — открыть
        if (gamepad1.a) {
            storageServo.setPosition(0.45);
        }

        // B — закрыть
        if (gamepad1.b) {
            storageServo.setPosition(0.2);
        }
    }
}