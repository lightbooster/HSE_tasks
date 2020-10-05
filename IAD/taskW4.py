import sys
import pathlib
import os
import numpy

class TextIterator:

    def __init__(self, dir_path, extension="txt", min_size=160):
        if not os.path.isdir(dir_path):
            print("ERROR:", dir_path, "is not a directory")
            return

        self.all_files = list()
        self.all_lines = list()
        self.__line_index = 0

        self.walk_the_dir(dir_path)
        self.sort_files_by_extension(extension)
        self.read_lines(min_size)

    def walk_the_dir(self, dir_path):
        items = os.listdir(dir_path)
        for item in items:
            item_name = dir_path + "/" + item
            if os.path.isfile(item_name):
                self.all_files.append(item_name)
            if os.path.isdir(item_name):
                self.walk_the_dir(item_name)

    def sort_files_by_extension(self, extension):
        self.all_files = [f for f in self.all_files if f[f.rfind('.') + 1:] == extension]

    def sort_files_by_size(self, size):
        new_list = list()
        for file in self.all_files:
            with open(file, 'r') as f:
                if len(f.read()) > size:
                    new_list.append(file)
        self.all_files = new_list

    def read_lines(self, min_size):
        for file in self.all_files:
            file_size = 0
            line_list_temp = list()
            for line in open(file):
                file_size += len(line)
                line_list_temp.append(line)
            print(file_size)
            if file_size >= min_size:
                self.all_lines.extend(line_list_temp)

    def __iter__(self):
        self.__line_index = 0
        return self

    def __next__(self):
        if (self.__line_index) >= len(self.all_lines) - 1:
            raise StopIteration
        self.__line_index += 1
        return self.all_lines[self.__line_index - 1]


def float_range(min: float, max:float, delimetr=0.1):
    if min > max:
        print("ERROR: min > max")
        return "ERRROR"

    my_num = min
    while True:
        yield my_num
        if my_num == max:
            return
        my_num += delimetr if my_num + delimetr < max else (max - my_num)


def time_range(min: tuple, max: tuple, delimetr=(0, 10, 0)):
    for time in [min, max, delimetr]:
        for n in time:
            if n >= 60 or n < 0 or time.index(n) > 2:
                print("ERROR: not a time")
                return
    my_time = min
    while True:
        yield my_time
        if my_time == max:
            return
        add = time_add(my_time, delimetr)
        my_time = add if add < max else max


def time_add(t1, t2, max=60, len=3):
    left = 0
    add = 0
    new_result = [0] * len
    for i in range(len-1, -1, -1):
        summa = t2[i] + t1[i] + add
        left = summa % max
        add = summa // max
        if left == summa:
            new_result[i] = summa
            left = 0
        else:
            new_result[i] = left

    return tuple(new_result)


def enumerate(collection):
    index = 0

    if type(collection) is (list or tuple):
        while True:
            if index >= len(collection):
                return
            yield (index, collection[index])
            index += 1

    elif type(collection) is dict:
        keys_list = list(collection.keys())
        while True:
            if index >= len(collection.values()):
                return
            yield (keys_list[index], collection[keys_list[index]])
            index += 1
    else:
        print("ERROR: not list, dict or tuple")
        return

# ### TASK 1 ###
# for line in TextIterator("/Users/sanduser/PycharmProjects/IAD/test"):
#     print(line, end='')

# ### TASK 2 ###
# for n in float_range(1, 2):
#     print(n)

# ### TASK 3 ###
# for n in time_range((1, 20, 0), (3, 0, 0), delimetr=(0,1,50)):
#     print(n)

# ### TASK 4 ###
for pair in enumerate(["lol", "kek"]):
    print(pair)

for pair in enumerate({'lol': 1, 'kek': 2}):
    print(pair)


