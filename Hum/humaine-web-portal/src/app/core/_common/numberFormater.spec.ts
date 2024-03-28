import { NumberFormaterPipe } from './numberFormater.pipe';

describe('NumberFormaterPipe', () => {
  it('create an instance', () => {
    const pipe = new NumberFormaterPipe();
    expect(pipe).toBeTruthy();
  });
});
