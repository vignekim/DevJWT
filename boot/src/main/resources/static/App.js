import axios from 'axios'
export default {
  data() {
    return {
      view: 1,
      key: '',
      auth: {
        token: '',
        header: {
          algorithm: '',
          type: ''
        },
        payload: {
          no: 0,
          name: '',
        }
      }
    }
  },
  methods: {
    authorize() {
      this.auth.token = ''
      if(this.key == '') return
      const params = {name: this.key};
      axios.get('/authorize', {params})
        .then((res) => {
          if(res.data.state) {
            this.auth.token = res.data.token
            this.key = ''
            this.view = 2
          }
        })
        .catch((err) => console.log(err))
    },
    verification() {
      if(this.auth.token == '') return
      const params = {token: this.auth.token};
      axios.post('/verification', params)
        .then((res) => {
          if(res.data.state) {
            this.auth.header = res.data.header
            this.auth.payload = res.data.payload
          } else {
            alert("token 유효 시간이 지났습니다.")
            this.clean()
          }
        })
        .catch((err) => console.log(err))
    },
    clean() {
      this.auth = {
        token: '',
        header: {algorithm: '',type: ''},
        payload: {name: ''}
      }
      this.view = 1
    }
  },
}