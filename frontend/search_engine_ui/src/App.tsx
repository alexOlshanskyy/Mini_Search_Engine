
import React, {ChangeEvent, Component, FormEvent} from 'react';


interface AppState {
    query : string

}
class App extends Component<{}, AppState> {

  constructor(props: any) {
    super(props);
    this.state = {query : ""};
  }

    updateSearch = (e: ChangeEvent<HTMLInputElement>) => {
        this.setState({ query: e.target.value});
    };

    onKey = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            this.search().then();
        }
    }

    async search() {
        this.setState({ query: ""});
        let res = await fetch("http://localhost:4567/search/" + this.state.query);
        if (!res.ok) {
            alert("Unable to find path");
            return;
        }
        let response = await res.json();
        console.log(response);
    }

  render() {
    return (
        <div>
          <p><b>My search engine</b></p>
            <input
                onKeyPress={this.onKey}
                placeholder="Search the Web"
                onChange={this.updateSearch}
                value={this.state.query}
            />
          <button onClick={this.search.bind(this)}>GO</button>
        </div>
    );
  }


}

export default App;
