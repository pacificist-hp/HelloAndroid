function log_func(msg1, msg2) {
    if (msg1) {
        log_msg(msg1);
    } else if (msg2) {
        log_msg(msg2);
    } else {
        var def = "hello if else";
        log_msg(def);
    }
}

function log_msg(msg) {
    log(msg);
}
