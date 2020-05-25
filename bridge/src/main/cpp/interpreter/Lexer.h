//
// Created by Administrator on 2020/4/25.
//

#ifndef HELLOANDROID_LEXER_H
#define HELLOANDROID_LEXER_H

#include <sstream>
#include <list>
#include <stack>

#include "../include/common.h"

namespace bridge {

    struct Character {
        Character(char c, int line) {
            _c = c;
            _line = line;
        }

        char _c;
        int _line;
    };

    typedef shared_ptr<Character> CharPtr;

    class Reader {
    public:
        Reader() {
            _line = 0;
            _idx = 0;
            _istream = nullptr;
        }

        ~Reader() {

        }

        void set_code(const char *code) {
            _line = 0;
            _idx = 0;
            _istream = make_shared<istringstream>(code);
        }

        // 不断读取当前行中的字符，头指针后移
        CharPtr read() {
            if (_idx >= _str.size()) {
                _idx = 0;
                _str = read_line();
                if (_str.size() == 0) {
                    return nullptr;
                }
            }

            return make_shared<Character>(_str.at(_idx++), _line);
        }

    private:
        string read_line() {
            string ret;
            // 读一行，保存在ret中
            getline(*_istream, ret);
            _line++;

            // 如果读到空行，则往下再读一行
            while (ret.empty()) {
                if (_istream->eof()) {
                    return ret;
                }
                return read_line();
            }

            ret += '\n';
            return ret;
        }

    private:
        // 当前行中的第几个字母
        int _idx;

        int _line;

        // 当前行的内容
        string _str;

        shared_ptr<istream> _istream;
    };

    typedef shared_ptr<Reader> ReaderPtr;

    class Lexer {
    public:
        Lexer() {

        }

        ~Lexer() {

        }

    public:
        void set_reader(ReaderPtr reader) {
            _reader = reader;
            _current_token = nullptr;
        }

        // 不断读取词语，头指针后移
        TokenPtr read() throw(BridgeException) {
            TokenPtr token = peek(0);
            if (token != nullptr) {
                _token_queue.pop_front();
                _current_token = token;
            }

            LOGD("Lexer::read: %s", token == nullptr ? "null" : token->description().c_str());
            return token;
        }

        TokenPtr last() {
            return _current_token;
        }

        // 读取指定位置的词语，头指针不动
        TokenPtr peek(int idx) {
            if (idx < 0) {
                return nullptr;
            }

            if (_token_queue.empty() || _token_queue.size() <= idx) {
                fill_queue();
            }

            TokenPtr ret = nullptr;
            if (!_token_queue.empty()) {
                if (idx == 0) {
                    ret = _token_queue.front();
                } else {
                    list<TokenPtr>::iterator it = _token_queue.begin();
                    while (idx--) {
                        it++;
                    }

                    ret = *it;
                }
            }

            return ret;
        }

    private:
        void fill_queue() throw(BridgeException) {
            TokenPtr token_word = read_token_word();
            while (token_word != nullptr) {
                _token_queue.push_back(token_word);

                if (_token_queue.size() > 10) {
                    break;
                }

                token_word = read_token_word();
            }
        }

        TokenPtr read_token_word() throw(BridgeException) {
            CharPtr chr = nullptr;
            do {
                chr = get_char();
            } while (is_space(chr));

            if (chr == nullptr) {
                _reader = nullptr;
                return nullptr;
            }

            if (is_digit(chr)) {
                return get_number_token(chr);
            }

            if (is_letter(chr) || chr->_c == '_') {
                return get_identifier_token(chr);
            }

            if (chr->_c == '=' || chr->_c == '<' || chr->_c == '>' || chr->_c == '!' ||
                chr->_c == '+' || chr->_c == '-' || chr->_c == '*' || chr->_c == '/') {
                return get_math_op_token(chr);
            }

            if (chr->_c == '&') {

            }

            if (chr->_c == '|') {

            }

            if (chr->_c == '\"') {
                return get_text_token(chr);
            }

            StringPtr word = make_shared<string>();
            *word = chr->_c;
            return make_shared<IdentifierToken>(word, chr->_line);
        }

        CharPtr get_char() {
            if (_reader == nullptr) {
                return nullptr;
            }

            if (_reversed_char.empty()) {
                CharPtr chr = _reader->read();
                return chr;
            } else {
                CharPtr chr = _reversed_char.top();
                _reversed_char.pop();
                return chr;
            }
        }

        TokenPtr get_number_token(CharPtr chr) {
            StringPtr word = make_shared<string>();
            while (is_digit(chr)) {
                *word += chr->_c;
                chr = get_char();
            }

            if (chr != nullptr && chr->_c == '.') {

            } else {
                reverse_char(chr);
            }

            return (*word).empty() ? nullptr : make_shared<IntToken>(atoi(word->c_str()), chr->_line);
        }

        TokenPtr get_identifier_token(CharPtr chr) {
            TokenPtr token = nullptr;
            StringPtr word = make_shared<string>();

            do {
                *word += chr->_c;
                chr = get_char();
            } while (is_letter(chr) || chr->_c == '_' || is_digit(chr));

            if (*word == "true") {

            } else if (*word == "false") {

            } else if (*word == "null") {

            } else {
                token = make_shared<IdentifierToken>(word, chr->_line);
            }

            reverse_char(chr);

            return token;
        }

        TokenPtr get_math_op_token(CharPtr chr) {
            if (chr == nullptr) {
                return nullptr;
            }

            StringPtr word = make_shared<string>();
            if (chr->_c == '/') {

            } else if (chr->_c == '+') {
                CharPtr next = get_char();
                if (next != nullptr && next->_c == '+') {
                    *word = "++";
                    return make_shared<IdentifierToken>(word, chr->_line);
                } else {
                    reverse_char(next);
                }
            } else if (chr->_c == '-') {
                CharPtr next = get_char();
                if (next != nullptr && next->_c == '-') {
                    *word = "--";
                    return make_shared<IdentifierToken>(word, chr->_line);
                } else {
                    reverse_char(next);
                }
            }

            *word += chr->_c;

            CharPtr next = get_char();
            if (next != nullptr && next->_c == '=') {
                *word += '=';
            } else {
                reverse_char(next);
            }

            return make_shared<IdentifierToken>(word, chr->_line);
        }

        TokenPtr get_text_token(CharPtr chr) {
            StringPtr word = make_shared<string>();
            while (true) {
                chr = get_char();
                if (chr->_c == '\n') {
                    throw BridgeException("string has no end identifier");
                }

                if (chr == nullptr || chr->_c == '\"') {
                    break;
                }

                if (chr->_c == '\\') {
                    CharPtr next = get_char();
                    if (next != nullptr) {
                        if (next->_c == '\"' || next->_c == '\\') {
                            *word += next->_c;
                        } else if (next->_c == 'n') {
                            *word += '\n';
                        } else {
                            reverse_char(next);
                            *word += chr->_c;
                        }
                    }
                } else {
                    *word += chr->_c;
                }
            }

            return make_shared<TextToken>(word, chr->_line);
        }

        void reverse_char(CharPtr chr) {
            if (chr != nullptr) {
                _reversed_char.push(chr);
            }
        }

        bool is_space(CharPtr chr) {
            return chr != nullptr && chr->_c >= 0 && chr->_c <= ' ';
        }

        bool is_digit(CharPtr chr) {
            return chr != nullptr && chr->_c >= '0' && chr->_c <= '9';
        }

        bool is_letter(CharPtr chr) {
            return chr != nullptr && ((chr->_c >= 'a' && chr->_c <= 'z') ||
                   (chr->_c >= 'A' && chr->_c <= 'Z'));
        }

    private:
        ReaderPtr _reader;
        TokenPtr _current_token;

        stack <CharPtr> _reversed_char;
        list <TokenPtr> _token_queue;
    };

    typedef shared_ptr<Lexer> LexerPtr;
}
#endif //HELLOANDROID_LEXER_H
