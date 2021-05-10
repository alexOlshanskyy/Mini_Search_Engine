
import React, {Component} from 'react';


interface AppState {

}
class App extends Component<{}, AppState> {

  constructor(props: any) {
    super(props);
    this.state = {};
  }

  render() {
    return (
        <div>
          <p><b>My search engine</b></p>
          <button onClick={this.search.bind(this)}>Search</button>
        </div>
    );
  }

  async search() {
      let res = await fetch("http://localhost:4567/getPath/t/t");
      if (!res.ok) {
        alert("Unable to find path");
        return;
      }
      let response = await res.json();
      console.log(response);
  }
}

export default App;
