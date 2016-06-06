<?php
class Secure {

	public static function genSalt() {
		return substr(str_replace('+', '.', base64_encode(sha1(microtime(true), true))), 0, 22);
	}

	public static function genHash($pass, $salt = null) {
		if (!isset($salt) || strlen($salt) != 22)
			$salt = self::genSalt();

		return crypt($pass, '$2a$12$' . $salt);
	}

  public static function testPassword($pass, $hash) {
    return $hash == crypt($pass, $hash);
  }

  public static function randStr($length = 32) {
    $chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $chars_len = strlen($chars);
    $str = '';

    for ($i = 0 ; $i < $length ; $i++)
      $str .= $chars[rand(0, $chars_len - 1)];

    return $str;
  }
}
