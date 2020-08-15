function log_func(num) {
    var big = 200;
    var small = 1;
    if (big >= num) {
        log("expression", big + " >= " + num);
    }

    if (small <= num) {
        log("expression", small + " <= " + num);
    }
}
