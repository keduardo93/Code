
<?php
$mysqli = new mysqli("localhost", "root");
if ($mysqli->connect_errno) {
    echo "Fallo al conectar a MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}
$mysqli->query("SET NAMES utf8");
$mysqli->query("SET CHARACTER SET utf8");
?>