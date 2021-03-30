import numpy as np
from sklearn.base import BaseEstimator, ClusterMixin
from scipy.spatial import distance
import itertools
import enum
from sklearn.datasets import load_iris


def get_condensed_distance(pair, condensed_matrix):
    i, j = pair
    size = condensed_matrix.size
    if i == j or not 0 <= i < size or not 0 <= j < size:
        return None
    pair = (i, j) if i <= j else (j, i)
    index = list(itertools.combinations(range(size), 2)).index(pair)
    return condensed_matrix[index]


class MyDBSCAN(BaseEstimator, ClusterMixin):
    """
    My implementation of DBSCAN clustering algorithm
    """

    def __init__(self, eps=0.5, min_samples=5, metric='euclidean',  metric_params=None):
        """
        Initialize MyDBSCAN with algorithm's params
        :param eps: (float) neighborhood range
        :param min_samples: (int) core object definition condition
        :param metric: (str) distance metric, or callable
        :param metric_params: (dict) additional params for metrics
        """
        self.eps = eps
        self.min_samples = min_samples
        self.metric = metric
        self.metric_params = metric_params if metric_params else {}

        self.labels_ = None
        self.core_sample_coords_ = None
        self.core_sample_indices_ = None
        self._objects_states_ = None

    class ObjectState(enum.Enum):
        outlier = -1
        reachable = 0
        core = 1

    def _define_clusters(self, objects_states: list, neighbours: list):
        """
        Define clusters based on objects' states (core, reachable, outlier)
        :param objects_states: list of ObjectState type; size of objects' num in dataset
        :param neighbours: nested list where all neighbours of objects (indexes) are listed
        :return: labels (np.array)
        """
        size = len(objects_states)
        new_labels = np.full(size, -1, dtype=int)
        cur_label = 0
        for object_i in range(size):
            if new_labels[object_i] != -1 or objects_states[object_i] is not self.ObjectState.core:
                continue
            my_stack = list()
            depth_i = object_i

            # depth search of connected objects
            while True:
                if new_labels[depth_i] == -1:
                    new_labels[depth_i] = cur_label
                    if objects_states[depth_i] is self.ObjectState.core:
                        for neighbour in neighbours[depth_i]:
                            if new_labels[neighbour] == -1:
                                my_stack.append(neighbour)
                if not len(my_stack):
                    break
                else:
                    depth_i = my_stack.pop()
            cur_label += 1

        return new_labels

    @staticmethod
    def _find_neighbours(X, eps, objects_num):
        """
        Find all neighbours for objects
        :param X: condensed distance matrix
        :param eps: neighborhood range - max distance for defining neighbor
        :param objects_num: number of objects
        :return: nested list where all neighbours of objects (indexes) are listed
        """
        neighbours = [[] for i in range(objects_num)]
        for pair, dist in zip(itertools.combinations(range(objects_num), 2), X):
            if dist <= eps:
                i, j = pair
                neighbours[j].append(i)
                neighbours[i].append(j)
        return neighbours

    def fit(self, X, y=None):
        """
        Define clusters based on DBSCAN algorithm
        :param X: np.array(shape=(samples, features)) of objects to cluster or distance_matrix
        :param y: Ignore
        :return: self
        """
        if self.metric == 'precomputed':
            distance_matrix = distance.squareform(X)
        else:
            distance_matrix = distance.pdist(X=X, metric=self.metric, **self.metric_params)
        size = X.shape[0]
        neighbours = self._find_neighbours(distance_matrix, self.eps, size)
        # self._objects_states_ = {i: self.ObjectState.outlier for i in range(size)}
        self._objects_states_ = [self.ObjectState.outlier] * size
        self.core_sample_coords_ = []
        self.core_sample_indices_ = []
        # define core objects
        for object_i in range(size):
            if len(neighbours[object_i]) + 1 >= self.min_samples:
                self._objects_states_[object_i] = self.ObjectState.core
                if self.metric != 'precomputed':
                    self.core_sample_coords_.append(X[object_i])
                    self.core_sample_indices_.append(object_i)
        # define reachable objects
        for object_i in range(size):
            if self._objects_states_[object_i] is self.ObjectState.core:
                continue
            for neighbour in neighbours[object_i]:
                if self._objects_states_[neighbour] is self.ObjectState.core:
                    self._objects_states_[object_i] = self.ObjectState.reachable
                    break
        # define clusters
        self.labels_ = self._define_clusters(self._objects_states_, neighbours)
        return self

    def fit_predict(self, X, y=None):
        """
        Define clusters based on DBSCAN algorithm and return labels
        :param X: np.array(shape=(samples, features)) or distance_matrix
        :param y: Ignore
        :return: np.array(shape=samples) labels of defined clusters
        """
        self.fit(X)
        return self.labels_

    def predict(self, X):
        """
        Predict cluster based on fitted MyDBSCAN
        :param X: np.array(shape=(samples, features)) of objects to predict cluster
        :return: labels for every object
        """
        if self.metric == 'precomputed':
            raise RuntimeError("MyDBSAN has been initialized with precomputed distance matrix. Cannot predict")
        if self.labels_ is None:
            raise RuntimeError("MyDBSAN must be fitted to predict")
        size = X.shape[0]
        dist_matrix = distance.cdist(X, self.core_sample_coords_)
        new_labels_ = np.full(size, -1, dtype=int)
        for object_i in range(size):
            unique_labels = set(self.labels_) - {-1}
            cluster_candidates = {label: 0 for label in unique_labels}
            for core_i in range(len(self.core_sample_coords_)):
                if dist_matrix[object_i][core_i] <= self.eps:
                    cur_label = self.labels_[self.core_sample_indices_[core_i]]
                    cluster_candidates[cur_label] += 1
            better_cluster, cluster_score = sorted(cluster_candidates.items(), key=lambda x: x[1])[-1]
            if cluster_score != 0:
                new_labels_[object_i] = better_cluster
        return new_labels_

    def get_params(self, deep=True):
        return {
            "eps": self.eps,
            "min_samples": self.min_samples,
            "metric": self.metric,
            "metric_params": self.metric_params
        }

    def set_params(self, **params):
        for parameter, value in params.items():
            setattr(self, parameter, value)
        return self


def main():
    iris = load_iris()
    X_train = iris.data
    y = iris.target

    my_clustering = MyDBSCAN()
    labels = my_clustering.fit_predict(X_train)
    print(labels)


if __name__ == "__main__":
    main()
