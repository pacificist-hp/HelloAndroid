function log_func(isDefault, msg) {
    var time = 3;
    if (isDefault) {
        var def = "hello default";
        log(def, time);
    } else {
        ++time;
        log(msg, time);
    }
}
