function log_func(num) {
    var big = 200;
    var small = 1;
    if (big >= num && small <= num) {
        log("expression", "good"); // good
    } else {
        /* bad */
        log("expression", "bad");
    }
}
