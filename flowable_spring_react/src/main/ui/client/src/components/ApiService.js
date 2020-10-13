import axios from 'axios';

const USER_API_BASE_URL = 'http://localhost:8081/ap/users';

class ApiService {

    fetchUsers = () => {
        return axios.get(USER_API_BASE_URL);
    }

    fetchUserById(userId) {
        return axios.get(USER_API_BASE_URL + '/' + userId);
    }
}

export default new ApiService();