function log_func(msg) {
    var time = 3;
    do {
        log(msg, time);
        --time;
    } while(time);

}
