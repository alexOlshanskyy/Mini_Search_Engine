
import React, {ChangeEvent, Component, FormEvent} from 'react';
import "./App.css";

interface AppState {
    query : string,
    results: any[] | null

}
class App extends Component<{}, AppState> {

  constructor(props: any) {
    super(props);
    this.state = {query : "", results : []};
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
        let r :any[] = response;
        console.log(r)
         this.setState({
             results: r
         })
        console.log(response);
    }

  render() {
    return (
        <div className="main">
            <div className="App2">
            <img src={"SearchEngineLogo.png"} height={100}/>
            </div>
            <div className="App1">

            <input
                className="input"
                onKeyPress={this.onKey}
                placeholder="Search the Web"
                onChange={this.updateSearch}
                value={this.state.query}
            />
            <button className={"go"} onClick={this.search.bind(this)} >GO</button>
            </div>
            {this.state.results && this.state.results.map(({link,score}, index) => (
                <p>{link}</p>
            ))}
        </div>
    );
  }


}

export default App;
