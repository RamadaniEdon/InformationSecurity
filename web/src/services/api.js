const BACKEND_URL =
    process.env.REACT_APP_BACKEND_URL || "http://localhost:5000/api";

// returns a Promise
function transformToJsonOrTextPromise(response) {
    const contentLength = response.headers.get("Content-Length");
    const contentType = response.headers.get("Content-Type");
    if (
        contentLength !== "0" &&
        contentType &&
        contentType.includes("application/json")
    ) {
        return response.json();
    } else {
        return response.text();
    }
}

async function sendRequest(url, { method = "GET", body, headers = {} }) {
    const options = {
        method,
        headers: new Headers({ "content-type": "application/json", ...headers }),
        body: body ? JSON.stringify(body) : null,
    };

    return fetch(url, options).then((res) => {
        const jsonOrTextPromise = transformToJsonOrTextPromise(res);

        if (res.ok) {
            return jsonOrTextPromise;
        } else {
            return jsonOrTextPromise.then(function (response) {
                const responseObject = {
                    status: res.status,
                    ok: false,
                    message: typeof response === "string" ? response : response.message,
                };

                return Promise.reject(responseObject);
            });
        }
    });
}

export async function getFeatureFlags(token) {
    return sendRequest(BACKEND_URL + `/users/featureFlags`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function fetchNumberOfYoungTrees(token) {
    return sendRequest(BACKEND_URL + `/trees/youngTreesCount`, {
        headers: {
            Authorization: "JWT " + token,
        },
    })
        .then((response) => response.data);
}

export async function fetchNumberOfOldTrees(token) {
    return sendRequest(BACKEND_URL + `/trees/oldTreesCount`, {
        headers: {
            Authorization: "JWT " + token,
        },
    })
        .then((response) => response.data);
}

export async function fetchWateredTreesPastInterval(days) {
    return sendRequest(BACKEND_URL + `/home/wateredCount/${days}`, {})
        .then((response) => response.data);
}

export async function fetchgetMonthlyAdoptionCount(token, year) {
    return sendRequest(BACKEND_URL + `/trees/getMonthlyAdoptionCount/${year}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    })
        .then((response) => response.data);
}

export async function fetchAdoptedTreesPastInterval(token, days) {
    return sendRequest(BACKEND_URL + `/trees/adoptedCount/${days}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    })
        .then((response) => response.data);
}

export async function fetchUserTreesNumber() {
    return sendRequest(BACKEND_URL + `/home/usersTreesCount`, {})
        .then((response) => response.data);
}

export async function fetchTreesNumber(token) {
    return sendRequest(BACKEND_URL + `/trees/treesCount`, {
        headers: {
            Authorization: "JWT " + token,
        },
    })
        .then((response) => response.data);
}

export async function adopt(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/adopt`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function water(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/water`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}
export async function getLastRainDate(token) {
    return sendRequest(BACKEND_URL + `/trees/getLastRainDate`, {
        method: "GET",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function renameTree(token, treeId, name) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/rename`, {
        method: "PATCH",
        body: {
            name,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function deleteTree(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/abandon`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function zipCodeSearch(token, zip) {
    return sendRequest(BACKEND_URL + `/trees/getTreeByZip/${zip}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function fetchTrees(token) {
    return sendRequest(BACKEND_URL + `/trees`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function fetchTypes(token) {
    return sendRequest(BACKEND_URL + `/trees/getTypes`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function renameType(token, treeId, type) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/changeType`, {
        method: "PATCH",
        body: {
            type,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function fetchTreesOfUser(token) {
    return sendRequest(BACKEND_URL + `/trees/user`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data.trees);
}

export async function fetchUserTreeData(token) {
    return sendRequest(BACKEND_URL + `/trees/getUserData`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function getTreeInfo(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/getTreeInfo`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function getTreeAllDetails(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/getTreeAllDetails`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function getTreeNickName(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/treenickname`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data.treeNickname);
}

export async function deleteUser(token) {
    return sendRequest(BACKEND_URL + `/users/delete/`, {
        method: "DELETE",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getAllUsers(token) {
    return sendRequest(BACKEND_URL + `/users/all`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function makeAdmin(token, userId) {
    return sendRequest(BACKEND_URL + `/users/admin/${userId}`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function removeAdmin(token, userId) {
    return sendRequest(BACKEND_URL + `/users/removeAdmin/${userId}`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function requestResetPassword(email) {
    return sendRequest(BACKEND_URL + "/users/reset-password", {
        method: "POST",
        body: {
            email,
        },
    });
}

export async function finishResetPassword(newPassword, resetPasswordToken) {
    return sendRequest(BACKEND_URL + "/users/reset-password/finish", {
        method: "POST",
        body: {
            newPassword,
            resetPasswordToken,
        },
    });
}

export async function login(email, password) {
    return sendRequest(BACKEND_URL + "/users/login", {
        method: "POST",
        body: {
            email,
            password,
        },
    });
}

export async function googleLogin(code) {
    return sendRequest(BACKEND_URL + "/users/google-login", {
        method: "POST",
        body: {
            code,
        },
    });
}

export async function signup(email, password) {
    return sendRequest(BACKEND_URL + "/users/signup", {
        method: "POST",
        body: {
            email,
            password
        },
    });
}

export async function verifyEmail(token) {
    return sendRequest(BACKEND_URL + "/users/verify-email", {
        method: "POST",
        body: {
            token,
        },
    });
}

export async function getTreesVersion(token) {
    return sendRequest(BACKEND_URL + `/trees/version`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function getNotSeenNotifications(token) {
    return sendRequest(BACKEND_URL + `/users/notifications/not-seen`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function setAllNotificationsToSeen(token) {
    return sendRequest(BACKEND_URL + `/users/notification/see`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function changeUsername(token, username) {
    return sendRequest(BACKEND_URL + `/users/change-username`, {
        method: "PATCH",
        body: {
            username,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function resetUsername(token, id) {
    return sendRequest(BACKEND_URL + `/users/reset-username`, {
        method: "PATCH",
        body: {
            id,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function fetchUserVisibility(token, userId) {
    return sendRequest(BACKEND_URL + `/users/${userId}/visibility`, {
        method: "GET",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function setUserVisibility(token, userId, visibility) {
    return sendRequest(BACKEND_URL + `/users/set-visibility`, {
        method: "PATCH",
        body: {
            userId,
            visibility,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function fetchUserTreeDataById(token, treeId) {
    return sendRequest(BACKEND_URL + `/trees/${treeId}/getUserTreeDetails`, {
        method: "GET",
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function fetchFeatures(token) {
    return sendRequest(BACKEND_URL + `/features`, {
        method: "GET",
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function fetchUserFeatures(token, userid) {
    return sendRequest(BACKEND_URL + `/users/${userid}/featureFlags`, {
        method: "GET",
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function enableFeature(token, featureId, userId) {
    return sendRequest(BACKEND_URL + `/users/enableFeature`, {
        method: "POST",
        body: {
            userId,
            featureId,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function disableFeature(token, featureId, userId) {
    return sendRequest(BACKEND_URL + `/users/disableFeature`, {
        method: "POST",
        body: {
            userId,
            featureId,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getVisibleUsers(token) {
    return sendRequest(BACKEND_URL + `/users/allVisible`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}


export async function getVisibleUserData(token, userId) {
    return sendRequest(BACKEND_URL + `/users/${userId}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response);
}

export async function resetMyUsername(token) {
    return sendRequest(BACKEND_URL + `/users/reset-username`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function sendFriendRequest(token, receiverEmail) {
    return sendRequest(BACKEND_URL + `/users/send-friend-request`, {
        method: "POST",
        body: {
            email: receiverEmail,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getUserFriendRequests(token, userId) {
    return sendRequest(BACKEND_URL + `/users/${userId}/friend-requests`, {
        headers: {
            Authorization: "JWT " + token,
        },
    }).then((response) => response.data);
}

export async function acceptFriendRequest(token, requestId) {
    return sendRequest(BACKEND_URL + `/users/friend-requests/accept`, {
        method: "PATCH",
        body: {
            id: requestId,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function ignoreFriendRequest(token, requestId) {
    return sendRequest(BACKEND_URL + `/users/friend-requests/ignore`, {
        method: "PATCH",
        body: {
            id: requestId,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getFriends(token, userId) {
    return sendRequest(BACKEND_URL + `/users/${userId}/friends`, {
        headers: {
            Authorization: "JWT " + token,
        }
    }).then((response) => response.data);
}

export async function isFriends(token, userId) {
    return sendRequest(BACKEND_URL + `/users/is-friend/${userId}`, {
        headers: {
            Authorization: "JWT " + token,
        }
    }).then((response) => response.data);
}

export async function removeFriend(token, userId) {
    return sendRequest(BACKEND_URL + `/users/remove-friend`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
        body: {
            id: userId,
        },
    });
}
export async function addToLeaderboard(token) {
    return sendRequest(BACKEND_URL + `/users/add-to-leaderboard`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}



export async function removeFromLeaderboard(token) {
    return sendRequest(BACKEND_URL + `/users/remove-from-leaderboard`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function fetchLeaderboard(token, userId) {
    return sendRequest(BACKEND_URL + `/users/fetch-leaderboard/${userId}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getLeaderboardUser(token, userId) {
    return sendRequest(BACKEND_URL + `/users/leaderboard-user/${userId}`, {
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function addPoints(token, points) {
    return sendRequest(BACKEND_URL + `/users/add-points`, {
        method: "PATCH",
        body: {
            points,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function subtractPoints(token, points) {
    return sendRequest(BACKEND_URL + `/users/subtract-points`, {
        method: "PATCH",
        body: {
            points,
        },
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function increaseStreak(token) {
    return sendRequest(BACKEND_URL + `/users/increase-streak`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function resetStreak(token) {
    return sendRequest(BACKEND_URL + `/users/reset-streak`, {
        method: "PATCH",
        headers: {
            Authorization: "JWT " + token,
        },
    });
}

export async function getKeyStore(password) {
    // return sendRequest(BACKEND_URL + `/trees/user`, {
    //     headers: {
    //         Authorization: "JWT " + token,
    //     },
    // }).then((response) => response.data.trees);
    return "1234";
}