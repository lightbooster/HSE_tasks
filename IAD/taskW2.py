import os
import sys
import random


def replace_char(string: str, index: int, new_string: str):
    return string[:index] + new_string + string[index+1:]


def number_finder():
    counters = list(0 for x in range(1001))

    for num in range(1, 1000):
        num_save = num
        counters[num] = 0
        while num_save != 1:
            counters[num] += 1
            if num_save % 2 == 0:
                num_save //= 2
            else:
                num_save = 3 * num_save + 1

    max_iterations = max(counters)
    print("Number '", counters.index(max_iterations), "' needs ", max_iterations, " iterations")


def get_input(correct_answers: list):
    answer = input()
    while answer not in correct_answers:
        print("ERROR: Undefined symbol")
        answer = input()
    return answer


def guess_number_ai(max_num=1001):
    print(" *********************************")
    print(" *** I WILL GUESS YOUR NUMBER! ***")
    print(" *********************************")
    print(" *** commands:                 ***")
    print(" '>' - to show your num is higher ")
    print(" '<' - to show your num is lower  ")
    print(" 'R' - to show your num is RIGHT  ")
    print(" *********************************")

    if max_num == 1001:
        max_num = input('Max number: ')
        try:
            max_num = int(max_num) + 1
        except Exception as e:
            print(e)
            max_num = 1001
    else:
        max_num += 1

    guess_list = list(range(max_num))
    usr_answer = ""
    commands = ['>', '<', 'R']

    print("***     O'KEY LET'S START!     ***")
    ai_answer = guess_list[len(guess_list)//2]
    print("Have you guessed number: ", ai_answer, "?")
    usr_answer = get_input(commands)

    while usr_answer != 'R':
        if usr_answer == '<':
            guess_list = guess_list[:guess_list.index(ai_answer)]
        else:
            guess_list = guess_list[guess_list.index(ai_answer) + 1:]

        if len(guess_list) == 1:
            print("YOUR NUMBER IS '", guess_list[0], "' !")
            return
        elif not guess_list:
            print(" YOU FOOLED ME :(")
            return

        ai_answer = guess_list[len(guess_list)//2]
        print("Have you guessed number: ", ai_answer, "?")
        usr_answer = get_input(commands)

    print(" I WIN! ")
    return


def guess_number_h(max_num=1001):
    print(" *********************************")
    print(" *** CAN YOU GUESS MY NUMBER?  ***")
    print(" *********************************")
    print(" *** type only integer numbers ***")
    print(" *********************************")

    if max_num == 1001:
        max_num = input('Max number: ')
        try:
            max_num = int(max_num) + 1
        except Exception as e:
            print(e)
            max_num = 1001
    else:
        max_num += 1

    guessed_num = random.randint(0, max_num)
    usr_answer = int(get_input(list(map(lambda x: str(x), list(range(max_num))))))
    counter = 1

    while usr_answer != guessed_num:
        counter += 1
        if usr_answer < guessed_num:
            print(" My num is >", usr_answer)
        if usr_answer > guessed_num:
            print(" My num is <", usr_answer)
        usr_answer = int(get_input(list(map(lambda x: str(x), list(range(max_num))))))

    print('YOU WIN!')
    print("I've guessed number", guessed_num)
    print("You needed", counter, "tries")


class TextAnalytic:

    PUNCTUATION_MARKS = ['.', '!', '?', '...', ',', "'", " - ", "..", '(', ')', ';', ':', ' ']
    PUNCT_MIN_NUM = 4

    def __init__(self):
        self.text = ""
        self.reversed_text = ""
        self.text_dict = dict()
        self.top_ten = list()
        self.words_num = 0
        self.sentences = list()
        self.words_in_sentences = list()

    def read_text(self, filename: str):
        with open(filename, "r") as file:
            self.text = file.read()

    def separate_sentences(self, source: str, punctuation_marks=None) -> list:
        if punctuation_marks is None:
            punctuation_marks = self.PUNCTUATION_MARKS[:self.PUNCT_MIN_NUM]

        temp_text = self.text if source is None else source

        while temp_text:
            first_punct = list()
            first_list = list(map(lambda x: temp_text.find(x), punctuation_marks))
            for num in first_list:
                if num != -1:
                    first_punct.append(num)
            first_punct = min(first_punct) if first_punct else len(temp_text)

            delta = 3 if temp_text[first_punct: first_punct+3] == '...' else 1
            self.sentences.append(temp_text[:first_punct+delta])
            temp_text = temp_text[first_punct+delta:]

        return self.sentences

    def separate_words(self, source: str) -> list:
        words_list = list()
        checkpoint = 0
        for char_i in range(len(source)):
            if source[char_i] in self.PUNCTUATION_MARKS:
                words_list.append(source[checkpoint:char_i]) if source[checkpoint:char_i] else None
                words_list.append(source[char_i])
                checkpoint = char_i + 1

        return words_list

    def create_sentences(self, source: str) -> list:
        if source is None:
            source = self.text

        self.words_in_sentences = list()
        self.separate_sentences(source)
        for sent in self.sentences:
            self.words_in_sentences.append(self.separate_words(sent.strip()))

        return self.words_in_sentences

    def count_words(self) -> list:
        if self.words_in_sentences is None:
            print("ERROR: use self.create_sentences() at first")
            return None

        self.text_dict = dict()

        for sent in self.words_in_sentences:
            for word in sent:
                if word and word not in self.PUNCTUATION_MARKS:
                    if word.lower() in self.text_dict.keys():
                        self.text_dict[word.lower()] += 1
                    else:
                        self.text_dict[word.lower()] = 1

        sorted_words = sorted(self.text_dict.items(), key=lambda kv: kv[1])
        return sorted_words[::-1]

    def reverse_text(self) -> str:
        if self.words_in_sentences is None:
            print("ERROR: use self.create_sentences() at first")
            return None

        reversed_text = ""

        for sent in self.words_in_sentences:
            for word in sent[::-1]:
                reversed_text += word
            reversed_text += ' '

        self.reversed_text = reversed_text
        return reversed_text


my_text = "Hey, Body! How are you? Wana play a game? Are there any balls in a house? A?"
my_class = TextAnalytic()

my_class.create_sentences(source=my_text)
# SHOW TOP 3
print(my_class.count_words()[:3])
# # print("ORIGINAL: ", my_class.text)
print("REVERSED: ", my_class.reverse_text())

# guess_number_ai(20)
# guess_number_h(20)

